package eu.cloudopting.bpmn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
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
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.Status;
import eu.cloudopting.dto.ActivitiDTO;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.dto.UploadDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.CloudAccountService;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.service.StatusService;
import eu.cloudopting.service.UserService;
import eu.cloudopting.service.util.StatusConstants;
import eu.cloudopting.store.StoreService;
//import scala.collection.concurrent.Debug;


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
	private StoreService storeService;

	@Inject
	private ApplicationService applicationService;
	
	@Inject
    private UserService userService;

	@Inject
    private CloudAccountService cloudAccountService;

	
    public UserService getUserService() {
        return userService;
    }

	public String startDeployProcess(String customizationId, long cloudId, boolean isTesting){
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
//		CloudAccounts account = theCust.getCloudAccount();
		CloudAccounts account = cloudAccountService.findOne(cloudId);
		Applications app = theCust.getApplicationId(); 

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
	
	public String returnZipPath(String pid){
		String zipPath = null;
		
		runtimeService.getVariable(pid, "serviceHome");
		
		return zipPath;
	}

	public void configuredVM(String processInstanceId){
		log.info("Before processing message");
		Map<String, Object> params = new HashMap<String, Object>();
    	unlockProcess(processInstanceId, "machineInstalled", params);
    	//Commented out, do we need to add Scala just to debug?
    	//Debug.log(executionIds.toString());
    	
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
	 */
	public Set<String> unlockProcess(String processInstanceId, String messageName, Map<String, ? extends Object> variables){
		log.debug("Unlocking Process with processInstanceId:'"+processInstanceId+"'");
		Set<String> result = new HashSet<String>();
		List<Execution> executions = runtimeService.createExecutionQuery()
			      .messageEventSubscriptionName(messageName).processInstanceId(processInstanceId)
			      .list();
			      
		for (Execution execution2 : executions) {
			String curExId = execution2.getId();
			result.add(curExId);
			runtimeService.setVariables(curExId, variables);
			runtimeService.messageEventReceived(messageName, curExId);
		}
		return result;
	}
	
	/**
	 * Unlocks the process instance with the provided instance id, waiting on the message event with the provided name.
	 * <strong>For testing purposes, might be removed at any time</strong>.
	 * @param processInstanceId The id of the instance of the target process
	 * @param processExecutionId The id of the execution of the target process 
	 * @param messageName The name of the intermediate message the instance is waiting for
	 */
	public Set<String> unlockProcess(String processInstanceId, String processExecutionId, String messageName, Map<String, ? extends Object> variables){
		log.debug("Unlocking Process with [processInstanceId:processExecutionId]=["+processInstanceId+":"+processExecutionId+"]");
		Set<String> result = new HashSet<String>();
		result.add(processInstanceId);
		runtimeService.setVariables(processInstanceId, variables);
		log.debug("---- Before Signaling "+messageName);
		runtimeService.messageEventReceived(messageName, processExecutionId);
		log.debug("---- After Signaling "+messageName);
		return result;
	}
	
	public byte[] getProcessStatusImage(String id){
		return null;
	}

	public ActivitiDTO startPublish(ApplicationDTO application, Organizations org) {
	    HashMap<String, Object> v = new HashMap<>();
	   
		v.put("application",application);
		v.put("org", org);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("ServicePublishingProcess",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("applicationId")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}
	
	/**
	 * Starts the Application Update process
	 * @param applicationId the DB id
	 * @param org
	 * @return
	 */
	public ActivitiDTO startUpdate(Long applicationId, Organizations org) {
	    HashMap<String, Object> v = new HashMap<>();
	    v.put("applicationId",applicationId);
		v.put("org", org);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("ServiceUpdateProcess",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		String appId = applicationId.toString();
		activitiDTO.setApplicationId(appId);
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
        
        String messageName = "",
        	   postMessageName = "",
        	   latestPathVariableName="";
        if (uploadType.equals(BpmnServiceConstants.SERVICE_FILE_TYPE_CONTENT_LIBRARY.toString())){
        	messageName = BpmnServiceConstants.MSG_START_ARTIFACTS_UPLOAD.toString();
        	postMessageName = BpmnServiceConstants.MSG_DONE_ARTIFACTS_UPLOAD.toString();
        	latestPathVariableName = "latestUploadedArtifactPath";
        }
        if (uploadType.equals(BpmnServiceConstants.SERVICE_FILE_TYPE_TOSCA_ARCHIVE.toString())){
        	messageName = BpmnServiceConstants.MSG_START_TOSCAFILE_UPLOAD.toString();
        	postMessageName = BpmnServiceConstants.MSG_DONE_TOSCAFILE_UPLOAD.toString();
        	latestPathVariableName = "latestUploadedToscaFilePath";
        }
        if (uploadType.equals(BpmnServiceConstants.SERVICE_FILE_TYPE_PROMO_IMAGE.toString())){
        	messageName = BpmnServiceConstants.MSG_START_PROMOIMAGE_UPLOAD.toString();
        	postMessageName = BpmnServiceConstants.MSG_DONE_PROMOIMAGE_UPLOAD.toString();
        	latestPathVariableName = "latestUploadedPromoImagePath";
        }
        
        //Return the updated value of the model
        ActivitiDTO activitiDTO = new ActivitiDTO();
		
        Execution exec = runtimeService.createExecutionQuery().processInstanceId(uploadProcessId).messageEventSubscriptionName(messageName).singleResult();
        if (exec!=null){
        	log.debug("Upload Execution:"+exec.toString()+", message:"+messageName);
        	String executionId = exec.getId();
            //Perform the upload
            unlockProcess(uploadProcessId, executionId , messageName, params);
            //Release the process by calling the intermediate message
            Map<String, Object> processVars = runtimeService.getVariables(exec.getId());
            String latestUploadPath = (String) processVars.get(latestPathVariableName);
            activitiDTO.setJrPath(latestUploadPath);
            runtimeService.messageEventReceived(postMessageName, uploadProcessId, processVars);
        }else{
        	log.warn("No Execution found for message:"+messageName);
        }
        
        activitiDTO.setApplicationId(uploadIdApp);
		activitiDTO.setProcessInstanceId(uploadProcessId);
//		Map map = ((ExecutionEntity) pi).getVariableInstances();
//		activitiDTO.setJrPath(jrPath);
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
        if (application.getStatus()==null){
        	log.warn("Status-less Application! No good.");
        	application.setStatus(statusDraft.getStatus());
        }
        String currentApplicationStatus = application.getStatus();
        
        Map<String, ApplicationDTO> params = new HashMap<String, ApplicationDTO>();
        params.put("application", application);
        
        String messageName = "";
        if (currentApplicationStatus!=null && currentApplicationStatus.equalsIgnoreCase(statusDraft.getStatus())){
        	messageName = BpmnServiceConstants.MSG_START_META_RETRIEVAL.toString();
        }
        if (currentApplicationStatus!=null && currentApplicationStatus.equalsIgnoreCase(statusRequested.getStatus())){
        	messageName = BpmnServiceConstants.MSG_START_PUBLISH.toString();
        }
        if (currentApplicationStatus!=null && currentApplicationStatus.equalsIgnoreCase(statusPublished.getStatus())){
        	messageName = BpmnServiceConstants.MSG_START_META_UPDATE.toString();
        }
        
        Execution exec = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).messageEventSubscriptionName(messageName).singleResult();
        if (exec!=null){
        	log.debug("Update Execution:"+exec.toString()+", message:"+messageName);
            //Perform the update
            unlockProcess(processInstanceId, exec.getId(), messageName, params);
        }else{
        	log.warn("No Execution found for message:"+messageName);
        }
        
        if (messageName.equalsIgnoreCase(BpmnServiceConstants.MSG_START_META_RETRIEVAL.toString())){
        	application = (ApplicationDTO) runtimeService.getVariable(processInstanceId, "application");
        }
        if (messageName.equalsIgnoreCase(BpmnServiceConstants.MSG_START_PUBLISH.toString())){
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
}
