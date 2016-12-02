package eu.cloudopting.bpmn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.Status;
import eu.cloudopting.domain.User;
import eu.cloudopting.dto.ActivitiDTO;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.dto.ImageDTO;
import eu.cloudopting.dto.MultipleUploadDTO;
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
	private static final String gatewayActivityId = "eventgateway2";
	
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteCustomizationPermission(#customizationId)")
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
//			v.put("cloudId", account.getProviderId().getProvider());
			// TODO fix this
			v.put("cloudId", 1);

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

	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #org.id)")
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

	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#uploadDTO.idApp)")
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
	    String uploadProcessInstanceId = uploadDTO.getProcessId();
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("name",uploadName);
		params.put("type",uploadType);
        params.put("fileId",uploadFileId);
        params.put("filePath",uploadTempFilePath);
        params.put("appId",uploadIdApp);
        params.put("processId",uploadProcessInstanceId);
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
		
        Execution preUploadExec = runtimeService.createExecutionQuery().processInstanceId(uploadProcessInstanceId).activityId(BpmnService.gatewayActivityId)/*.messageEventSubscriptionName(messageName)*/.singleResult();
        if (preUploadExec!=null){
        	log.debug("ProcessInstanceId:"+uploadProcessInstanceId+", Upload Execution:"+preUploadExec.toString()+", message:"+messageName);
        	String executionId = preUploadExec.getId();
            //Perform the upload
            runtimeService.messageEventReceived(messageName, executionId, params);
        }else{
        	log.warn("[Pre Upload] No Execution found for message:"+messageName);
        }
        //Release the process by calling the POST UPLOAD intermediate message
        Map<String, Object> processVars = runtimeService.getVariables(uploadProcessInstanceId);
        String latestUploadPath = (String) processVars.get(latestPathVariableName);
        activitiDTO.setJrPath(latestUploadPath!=null?latestUploadPath:"");
        Execution postUploadExecution = runtimeService.createExecutionQuery().processInstanceId(uploadProcessInstanceId).messageEventSubscriptionName(postMessageName).singleResult();
        if (postUploadExecution!=null){
            	String pueId = postUploadExecution.getId();
            	log.debug("Post Upload Execution ID:"+pueId+".");
            	runtimeService.messageEventReceived(postMessageName, pueId, processVars);
        }else{
        	log.warn("[Post Upload] No Execution found for message:"+postMessageName);
        }
        activitiDTO.setApplicationId(uploadIdApp);
		activitiDTO.setProcessInstanceId(uploadProcessInstanceId);
		return activitiDTO;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#dto.idApp)")
	public ActivitiDTO uploadMultiple(MultipleUploadDTO uploadDTO) {
		//String uploadName = uploadDTO.getName();
	    String uploadType = uploadDTO.getType();
	    String uploadFileId = uploadDTO.getFileId();
	    List<ImageDTO> images = uploadDTO.getImages();
	    String uploadTempFilePath = "";
	    
	    List<Pair> filesList = new ArrayList<Pair>();
	    for(ImageDTO img : images) {
	    	try {
		    	File f = BpmnService.stream2file(img.getName(), img.getInputStream());
		    	if (f!=null){
		    		uploadTempFilePath = f.getAbsolutePath();
		    	}
		    	Pair pair = Pair.of(img.getName(), uploadTempFilePath);
		    	filesList.add(pair);
			} catch (IOException e) {
				log.error("Error while reading UPLOAD file.",e);
			}
	    }
	    
	    
	    String uploadIdApp = uploadDTO.getIdApp();
	    String uploadProcessInstanceId = uploadDTO.getProcessId();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type",uploadType);
        params.put("fileId",uploadFileId);
        params.put("appId",uploadIdApp);
        params.put("processId",uploadProcessInstanceId);
        params.put("org",uploadDTO.getOrg());
        params.put("user",uploadDTO.getUser());
        params.put("filesList", filesList);
        
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
		
        Execution preUploadExec = runtimeService.createExecutionQuery().processInstanceId(uploadProcessInstanceId).activityId(BpmnService.gatewayActivityId)/*.messageEventSubscriptionName(messageName)*/.singleResult();
        if (preUploadExec!=null){
        	log.debug("ProcessInstanceId:"+uploadProcessInstanceId+", Upload Execution:"+preUploadExec.toString()+", message:"+messageName);
        	String executionId = preUploadExec.getId();
            //Perform the upload
            runtimeService.messageEventReceived(messageName, executionId, params);
        }else{
        	log.warn("[Pre Upload] No Execution found for message:"+messageName);
        }
        //Release the process by calling the POST UPLOAD intermediate message
        Map<String, Object> processVars = runtimeService.getVariables(uploadProcessInstanceId);
        String latestUploadPath = (String) processVars.get(latestPathVariableName);
        activitiDTO.setJrPath(latestUploadPath!=null?latestUploadPath:"");
        Execution postUploadExecution = runtimeService.createExecutionQuery().processInstanceId(uploadProcessInstanceId).messageEventSubscriptionName(postMessageName).singleResult();
        if (postUploadExecution!=null){
            	String pueId = postUploadExecution.getId();
            	log.debug("Post Upload Execution ID:"+pueId+".");
            	runtimeService.messageEventReceived(postMessageName, pueId, processVars);
        }else{
        	log.warn("[Post Upload] No Execution found for message:"+postMessageName);
        }
        activitiDTO.setApplicationId(uploadIdApp);
		activitiDTO.setProcessInstanceId(uploadProcessInstanceId);
		return activitiDTO;
	}
	
	
	

	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#uploadDTO.idApp)")
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
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#applicationDTO.id)")
	public boolean updateApplicationLogo(ApplicationDTO application, MultipartFile newLogoFile, String name, String type, User user, Organizations org) {
		String uploadTempFilePath = "";
		try {
			File f = BpmnService.stream2file(name, newLogoFile.getInputStream());
			if (f!=null){
	    		uploadTempFilePath = f.getAbsolutePath();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error while reading UPLOAD logo file.",e);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("type", type);
		params.put("user", user);
		params.put("org", org);
		params.put("newlogopath", uploadTempFilePath);
		params.put("applicationid", application.getId());
		
		ProcessInstance pi = runtimeService.startProcessInstanceByMessage("updatePromoImageMsgRef", params);
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		Boolean bol = new Boolean(((VariableInstanceEntity)map.get("applicationlogoupdatedsuccess")).getTextValue());
		return bol;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#applicationDTO.id)")
	public boolean updateToscaFile(ApplicationDTO application, MultipartFile newToscaFile, String name, String type,
			User user, Organizations org) {
		String uploadTempFilePath = "";
		try {
			File f = BpmnService.stream2file(name, newToscaFile.getInputStream());
			if (f!=null){
	    		uploadTempFilePath = f.getAbsolutePath();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error while reading UPLOAD tosca file.",e);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("type", type);
		params.put("user", user);
		params.put("org", org);
		params.put("newtoscapath", uploadTempFilePath);
		params.put("applicationid", application.getId());
		
		ProcessInstance pi = runtimeService.startProcessInstanceByMessage("updateToscaFileMsgRef", params);
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		Boolean bol = new Boolean(((VariableInstanceEntity)map.get("applicationtoscafileupdatedsuccess")).getTextValue());
		return bol;
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#applicationDTO.id)")
	public boolean updateApplicationMetadata(ApplicationDTO application, User user, Organizations org) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put("org", org);
		params.put("application", application);
		
		ProcessInstance pi = runtimeService.startProcessInstanceByMessage("updateMetadataMsgRef", params);
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		Boolean bol = new Boolean(((VariableInstanceEntity)map.get("applicationmetadataupdatedsuccess")).getTextValue());
		return false;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#applicationDTO.id)")
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

	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#application.id)")
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
        
        Execution exec = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).activityId(BpmnService.gatewayActivityId)./*messageEventSubscriptionName(messageName).*/singleResult();
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
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteCustomizationPermission(#customizationId)")
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

	@PreAuthorize("hasRole('ROLE_ADMIN') or @bpmnAuthorization.hasWriteApplicationPermission(#applicationDTO.id)")
	public boolean addMediaFile(ApplicationDTO application, MultipartFile mediaFile, String name, String type,
			User user, Organizations org) {
		String uploadTempFilePath = "";
		try {
			File f = BpmnService.stream2file(name, mediaFile.getInputStream());
			if (f!=null){
	    		uploadTempFilePath = f.getAbsolutePath();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error while reading UPLOAD tosca file.",e);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("type", type);
		params.put("user", user);
		params.put("org", org);
		params.put("mediafilepath", uploadTempFilePath);
		params.put("applicationid", application.getId());
		
		ProcessInstance pi = runtimeService.startProcessInstanceByMessage("updateArtifactAddMsgRef", params);
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		Boolean bol = new Boolean(((VariableInstanceEntity)map.get("addmediafilesuccess")).getTextValue());
		return bol;
	}

	public boolean deleteMediaFile(ApplicationDTO application, Long idMedia, User user, Organizations org) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put("org", org);
		params.put("applicationid", application.getId());
		params.put("mediaid", idMedia);
		
		ProcessInstance pi = runtimeService.startProcessInstanceByMessage("updateArtifactDelMsgRef", params);
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		Boolean bol = new Boolean(((VariableInstanceEntity)map.get("deletemediafilesuccess")).getTextValue());
		return bol;
	}

	

	
	
}
