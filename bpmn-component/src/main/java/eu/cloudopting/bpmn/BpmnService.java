package eu.cloudopting.bpmn;

import java.io.File;
import java.io.FileOutputStream;
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

import scala.collection.concurrent.Debug;
import eu.cloudopting.bpmn.dto.BasicProcessInfo;
import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.Status;
import eu.cloudopting.dto.ActivitiDTO;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.dto.CustomizationDTO;
import eu.cloudopting.dto.UploadDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.service.StatusService;
import eu.cloudopting.service.util.StatusConstants;


@Service
@Transactional
public class BpmnService {
	private final Logger log = LoggerFactory.getLogger(BpmnService.class);
	
	public static final String TEMP_FILE_NAME_SEPARATOR = "___";

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

	public String startDeployProcess(String customizationId, String cloudId, boolean isTesting){
		log.info("Before activating process");
		log.info("customizationId: "+customizationId);
		log.info("cloudId: "+cloudId);
		log.info("isTesting: "+isTesting);
		
		
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
		Applications app = applicationService.findOne(theCust.getApplicationId()); 

		if(theCust.getProcessId()!= null){
			log.debug("Customization "+customizationId+" has already a deployment process");
			return theCust.getProcessId();
		}
		
		// Prepare the hash of data for the process
		HashMap<String, Object> v = new HashMap<String, Object>();
		
		if (isTesting){
			
		}else{
			v.put("cloudId", account.getProviderId().getProvider());
			v.put("cloudAccountId", account.getId());
			cloudService.setUpCloud(account.getApiKey(), account.getSecretKey(), account.getEndpoint(), account.getProviderId().getProvider(), account.getId());
			log.info("apikey: "+account.getApiKey());
			log.info("secretkey: "+account.getSecretKey());
			log.info("cloudEndpoint: "+account.getEndpoint());
			log.info("account_id: "+account.getId());		
		}
		
		
		
//		v.put("toscaFile", toscaId);
		v.put("customizationId", customizationId);
		v.put("isTesting", isTesting);
		v.put("cloudId", cloudId);
		v.put("toscaCsarPath", app.getApplicationToscaTemplate());
		
		
		
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
	
//	public String startTestDeployProcess(){
//		return null;
//	
//	}
//	
//	public String startAddApplicationProcess(){
//		return null;
//	
//	}
//	
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
	
	public static File stream2file (String uploadName, InputStream in) throws IOException {
        final File tempFile = File.createTempFile(uploadName+BpmnService.TEMP_FILE_NAME_SEPARATOR, "tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

	public ActivitiDTO upload(UploadDTO uploadDTO) {

		String uploadName = uploadDTO.getName();
	    String uploadType = uploadDTO.getType();
	    String uploadFileId = uploadDTO.getFileId();
	    String uploadTempFilePath = "";
	    try {
	    	File f = BpmnService.stream2file(uploadName, uploadDTO.getFile());
	    	if (f!=null){
	    		uploadTempFilePath = f.getAbsolutePath();
	    	}
		} catch (IOException e) {
			log.error("Error while reading UPLOAD file.",e);
		}
	    String uploadIdApp = uploadDTO.getIdApp();
	    String uploadProcessId = uploadDTO.getProcessId();
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("name",uploadName);
		params.put("type",uploadType);
        params.put("fileId",uploadFileId);
        params.put("filePath",uploadTempFilePath);
        params.put("appId",uploadIdApp);
        params.put("processId",uploadProcessId);
        params.put("org",uploadDTO.getOrg());
        params.put("user",uploadDTO.getUser());
        //The return value is not used, just for debug purposes
        Set<String> executionIds = null;
        if (uploadType.equals(BpmnServiceConstants.SERVICE_FILE_TYPE_CONTENT_LIBRARY.toString())){
        	executionIds = unlockProcess(uploadProcessId, "ArtifactsUploadEventRef", params);
        	Execution execution = runtimeService.createExecutionQuery()
        			  .processInstanceId(uploadProcessId)
        			  .activityId("postArtifactsUploadId")
        			  .singleResult();
  	      	//application = (ApplicationDTO) runtimeService.getVariable(uploadProcessId, "application");
  	      	runtimeService.signal(execution.getId());
        }
        if (uploadType.equals(BpmnServiceConstants.SERVICE_FILE_TYPE_TOSCA_ARCHIVE.toString())){
        	executionIds = unlockProcess(uploadProcessId, "ToscaUploadEventRef", params);
        	Execution execution = runtimeService.createExecutionQuery()
      			  .processInstanceId(uploadProcessId)
      			  .activityId("postToscaUploadId")
      			  .singleResult();
	      	//application = (ApplicationDTO) runtimeService.getVariable(uploadProcessId, "application");
	      	runtimeService.signal(execution.getId());
        }
        //Return the updated value of the model
        ActivitiDTO activitiDTO = new ActivitiDTO();
		activitiDTO.setApplicationId(uploadIdApp);
		activitiDTO.setProcessInstanceId(uploadProcessId);
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

	public ActivitiDTO deleteApplication(ApplicationDTO applicationDTO) {
		HashMap<String, Object> v = new HashMap<>();
		v.put("applicationdto",applicationDTO);
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
		Status statusDraft = statusService.findOne(StatusConstants.DRAFT),
        		statusRequested = statusService.findOne(StatusConstants.REQUESTED),
        		statusPublished = statusService.findOne(StatusConstants.PUBLISHED);
        Set<String> executionIds = new HashSet<String>();
        if (application.getStatus()==null){
        	log.warn("Status-less Application! No good.");
        	application.setStatus(statusDraft.getStatus());
        }
        String currentApplicationStatus = application.getStatus();
        
        Map<String, ApplicationDTO> params = new HashMap<String, ApplicationDTO>();
        params.put("application", application);
        
        if (currentApplicationStatus!=null && currentApplicationStatus.equalsIgnoreCase(statusDraft.getStatus())){
        	executionIds = unlockProcess(processInstanceId, "metadataRetrievalMsg", params);
        	Execution execution = runtimeService.createExecutionQuery()
        			  .processInstanceId(processInstanceId)
        			  .activityId("postMetadataTaskId")
        			  .singleResult();
        	application = (ApplicationDTO) runtimeService.getVariable(processInstanceId, "application");
        	runtimeService.signal(execution.getId());
        }
        if (currentApplicationStatus!=null && currentApplicationStatus.equalsIgnoreCase(statusRequested.getStatus())){
        	executionIds = unlockProcess(processInstanceId, "PublishEventRef", params);
        	//TODO Check for the return status from the process, if any
        	application.setStatus(statusPublished.getStatus());
        }
        ActivitiDTO activitiDTO = new ActivitiDTO();
        //The return value is not used, just for debug purposes
        if (currentApplicationStatus!=null && !currentApplicationStatus.equalsIgnoreCase(statusRequested.getStatus())){
        	ExecutionQuery eq = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).processVariableValueEquals("applicationId", application.getId().toString());
            List<Execution> executions = eq.list();
            //TODO Refactor this and remove all duplications/instantiations
    		String custId = (String) runtimeService.getVariable(processInstanceId, "customizationId");
    		activitiDTO.setCustomizationId(custId);
    		activitiDTO.setProcessInstanceId(processInstanceId);
    		Long appId = (Long) runtimeService.getVariable(processInstanceId, "applicationId");
    		activitiDTO.setApplicationId(appId.toString());
            //Return the updated value of the model
        }
        
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
