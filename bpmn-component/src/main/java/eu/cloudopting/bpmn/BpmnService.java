package eu.cloudopting.bpmn;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.bpmn.dto.BasicProcessInfo;
import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.dto.ActivitiDTO;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.dto.CustomizationDTO;
import eu.cloudopting.dto.UploadDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.service.StatusService;
import scala.collection.concurrent.Debug;


@Service
@Transactional
public class BpmnService {
	private final Logger log = LoggerFactory.getLogger(BpmnService.class);


	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;
    
    @Autowired
    protected ProcessEngineConfiguration processEngineConfiguration;
    
	@Autowired
	CloudService cloudService;
    
    @Autowired
    protected CustomizationService customizationS;
    
	@Inject
	private StatusService statusService;

	@Inject
	private ApplicationService applicationService;

	public String startDeployProcess(String customizationId, String cloudId){
		log.info("Before activating process");
		log.info("customizationId: "+customizationId);
		log.info("cloudId: "+cloudId);
		
		
		// the UI will pass the customization ID and the cloud ID since is in that page
		// will need to retrieve the:
		// TOSCA csar to unzip it
		// tosca customization to send it to the tosca service
		// the customer name to build folders
		// all this need to go in the process so I send the customization ID to the process
//		log.debug("\ncustomerId:"+customerId);
//		System.out.println("\ncloudId:"+cloudId);
		// We recover the Customization and chack if processId is null otherwise we need to throw exception since we cannot execute another deploy for the same Customization
		Customizations theCust = customizationS.findOne(Long.parseLong(customizationId));
		log.info("theCust: "+theCust.toString());
		CloudAccounts account = theCust.getCloudAccount();

		if(theCust.getProcessId()!= null){
			log.debug("Customization "+customizationId+" has already a deployment process");
			return theCust.getProcessId();
		}
		
		HashMap<String, Object> v = new HashMap<String, Object>();
//		v.put("toscaFile", toscaId);
		v.put("customizationId", customizationId);
		v.put("cloudId", cloudId);
		v.put("cloudId", account.getProviderId().getProvider());
		v.put("cloudAccountId", account.getId());
		cloudService.setUpCloud(account.getApiKey(), account.getSecretKey(), account.getEndpoint(), account.getProviderId().getProvider(), account.getId());
		
		log.info("apikey: "+account.getApiKey());
		log.info("secretkey: "+account.getSecretKey());
		log.info("cloudEndpoint: "+account.getEndpoint());
		log.info("account_id: "+account.getId());
		
		// TODO the process string has to go in a constant
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("cloudoptingProcess",v);
        System.out.println("ProcessID:"+pi.getProcessInstanceId());
        return pi.getProcessInstanceId();

	}

	public void configuredVM(String processInstanceId){
		log.info("Before processing message");
		Map<String, Object> params = new HashMap<String, Object>();
    	Set<String> executionIds = unlockProcess(processInstanceId, "machineInstalled", params);
    	Debug.log(executionIds.toString());
    	
	}

	
	/**
	 * Starts the process with the provided id and the provided initial input parameters.
	 * <strong>For testing purposes, might be removed at any time</strong>.
	 * @param processId the Identifier of the process (not null)
	 * @param startParams the input variables (might be null)
	 * @return the process instance id
	 */
	public String startGenericProcess(String processId, Map<String, Object> startParams){
		log.debug("Starting Process with id:'"+processId+"'");
		// TODO the process string has to go in a constant
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processId, startParams);
        System.out.println("ProcessID:"+pi.getProcessInstanceId());
        return pi.getProcessInstanceId();

	}
	
	/**
	 * Gets the list of active Process Definitions
	 * For testing purposes, <strong>might be removed at any time</strong>.
	 * @return
	 */
	public List<BasicProcessInfo> getAvailableProcessDefinitions(){
		log.debug("Retrieving available process definitions");
		RepositoryService rs = processEngine.getRepositoryService();
		List<BasicProcessInfo> result = new LinkedList<BasicProcessInfo>();
		for (ProcessDefinition currentDefinition : rs.createProcessDefinitionQuery().active().list()) {
			BasicProcessInfo bpi = new BasicProcessInfo(
						currentDefinition.getId(), 
						currentDefinition.getName(), 
						currentDefinition.getKey(), 
						currentDefinition.getVersion(), 
						currentDefinition.getDeploymentId()
			);
			result.add(bpi);
		}
        return result;
	}
	
	public void deleteDeploymentById(String deploymentId){
		log.debug("Deleting Process Deployment with id:"+ deploymentId);
		RepositoryService rs = processEngine.getRepositoryService();
		rs.deleteDeployment(deploymentId, true);
	}
	
	/**
	 * Unlocks the process instance with the provided instance id, waiting on the message event with the provided name.
	 * <strong>For testing purposes, might be removed at any time</strong>.
	 * @param processInstanceId The id of the instance of the target process
	 * @param messageName The name of the intermediate message the instance is waiting for
	 * @return A set containing the execution ids of unlocked process instances
	 */
	public Set<String> unlockProcess(String processInstanceId, String messageName, Map<String, ? extends Object> variables){
		Set<String> exIds = new HashSet<String>();
		log.debug("Unlocking Process with processInstanceId:'"+processInstanceId+"'");
		List<Execution> executions = runtimeService.createExecutionQuery()
			      .messageEventSubscriptionName(messageName).processInstanceId(processInstanceId)
			      .list();
			      
		for (Execution execution2 : executions) {
			String curExId = execution2.getId();
			exIds.add(curExId);
			runtimeService.setVariables(curExId, variables);
			runtimeService.messageEventReceived(messageName, curExId);
		}
		return exIds;
	}
	
	public String startTestDeployProcess(){
		return null;
	
	}
	
	public String startAddApplicationProcess(){
		return null;
	
	}
	
	public byte[] getProcessStatusImage(String id){
		return null;
	
	}
	

	public ActivitiDTO startPublish(ApplicationDTO application) {
	    HashMap<String, Object> v = new HashMap<>();
		v.put("application",application);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("ServicePublishingProcess",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("applicationId")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}

	public ActivitiDTO upload(UploadDTO uploadDTO) {
		byte[] uploadFileBytes = null;
		try {
			uploadFileBytes = IOUtils.toByteArray(uploadDTO.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String uploadName = uploadDTO.getName();
	    String uploadType = uploadDTO.getType();
	    String uploadFileId = uploadDTO.getFileId();
	    String uploadIdApp = uploadDTO.getIdApp();
	    String uploadProcessId = uploadDTO.getProcessId();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileBytes",uploadFileBytes);
        params.put("name",uploadName);
		params.put("type",uploadType);
        params.put("fileId",uploadFileId);
        params.put("appId",uploadIdApp);
        params.put("processId",uploadProcessId);
        params.put("org",uploadDTO.getOrg());
        params.put("user",uploadDTO.getUser());
        //The return value is not used, just for debug purposes
        //ToscaUploadEventRef
        Set<String> executionIds = null;
        if (uploadType.equals(BpmnServiceConstants.SERVICE_FILE_TYPE_CONTENT_LIBRARY)){
        	executionIds = unlockProcess(uploadProcessId, "ArtifactsUploadEventRef", params);
        }
        if (uploadType.equals(BpmnServiceConstants.SERVICE_FILE_TYPE_TOSCA_ARCHIVE)){
        	executionIds = unlockProcess(uploadProcessId, "ToscaUploadEventRef", params);
        }
      //The return value is not used, just for debug purposes
        ExecutionQuery eq = runtimeService.createExecutionQuery().processInstanceId(uploadProcessId).processVariableValueEquals("applicationId", uploadIdApp);
        List<Execution> executions = eq.list();
        //Return the updated value of the model
        ActivitiDTO activitiDTO = new ActivitiDTO();
//		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("applicationId")).getTextValue());
//		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		activitiDTO.setApplicationId(uploadIdApp);
		activitiDTO.setProcessInstanceId(uploadProcessId);
//		ProcessInstance pi = runtimeService.startProcessInstanceByKey("uploadProcess",v);
//		ActivitiDTO activitiDTO = new ActivitiDTO();
//		Map map = ((ExecutionEntity) pi).getVariableInstances();
//		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("uploadid")).getTextValue());
//		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}

	public ActivitiDTO deleteFile(UploadDTO uploadDTO) {
		HashMap<String, Object> v = new HashMap<>();
		v.put("uploaddto",uploadDTO);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("deleteFile",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("deletedfileid")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}

	public ActivitiDTO deleteApplication(ApplicationDTO uploadDTO) {
		HashMap<String, Object> v = new HashMap<>();
		v.put("applicationdto",uploadDTO);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("deleteApplication",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("applicationdeletedid")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}

	public ActivitiDTO updateApplication(ApplicationDTO application, String processInstanceId) {
		HashMap<String, Object> v = new HashMap<>();
		v.put("application",application);
		//ProcessInstance pi = runtimeService.startProcessInstanceByKey("updateApplication",v);
		Map<String, ApplicationDTO> params = new HashMap<String, ApplicationDTO>();
        params.put("application", application);
        //The return value is not used, just for debug purposes
        Set<String> executionIds = unlockProcess(processInstanceId, "metadataRetrievalMsg", params);
        //The return value is not used, just for debug purposes
        ExecutionQuery eq = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).processVariableValueEquals("applicationId", application.getId().toString());
        List<Execution> executions = eq.list();
        //Return the updated value of the model
        ActivitiDTO activitiDTO = new ActivitiDTO();
//		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("applicationId")).getTextValue());
//		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		activitiDTO.setApplicationId(application.getId().toString());
		activitiDTO.setProcessInstanceId(processInstanceId);
		return activitiDTO;
	}

	public ActivitiDTO createCustomization(CustomizationDTO customizationDTO) {
		HashMap<String, Object> v = new HashMap<>();
		v.put("customization",customizationDTO);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("createCustomization",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setCustomizationId(((VariableInstanceEntity) map.get("customizationId")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}

	public ActivitiDTO deleteCustomization(String customizationId) {
		HashMap<String, Object> v = new HashMap<>();
		v.put("customizationid",customizationId);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("deleteCustomization",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setCustomizationId(((VariableInstanceEntity) map.get("customizationId")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}

	public ActivitiDTO updateCustomization(CustomizationDTO customizationDTO) {
		HashMap<String, Object> v = new HashMap<>();
		v.put("customization",customizationDTO);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("updateCustomization",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setCustomizationId(((VariableInstanceEntity) map.get("customizationId")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}
}
