package eu.cloudopting.bpmn.tasks.publish;

import java.io.File;
import java.util.Set;

import javax.inject.Inject;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.domain.ApplicationMedia;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.User;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.service.ApplicationMediaService;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.store.StoreService;

@Service
public class PublishArtifactStorageTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishArtifactStorageTask.class);
	@Autowired(required=true)
	StoreService storeService;
	
	@Inject
	private ApplicationService applicationService;
	
	@Inject
	private  ApplicationMediaService applicationMediaService;
	
	
	@Autowired
    private RuntimeService runtimeService;
	
	/**
	 * Adds an entry to the set of Artifacts associated to this Application
	 * according to the format for JackRabbit
	 * @param execution
	 * @param orgKey
	 * @param toscaName
	 * @param remoteFileNameReduced
	 */
	private void addArtifactPath(DelegateExecution execution, String orgKey, String toscaName, String remoteFileNameReduced){
		ApplicationDTO applicationSource = (ApplicationDTO) execution.getVariable("application");
        Applications application = applicationService.findOne(applicationSource.getId());
        Set<ApplicationMedia> medias = application.getApplicationMedias();
        ApplicationMedia newMedium = new ApplicationMedia();
        newMedium.setApplicationId(application);
        newMedium.setMediaContent(storeService.getTemplatePath(orgKey,toscaName,true)+"/"+remoteFileNameReduced);
        medias.add(newMedium);
        applicationMediaService.create(newMedium);
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Publish - Artifacts Storage");
		String uploadName = (String) execution.getVariable("name");
	    String uploadType = (String) execution.getVariable("type");
	    String uploadFileId = (String) execution.getVariable("fileId");
	    String uploadFilePath = (String) execution.getVariable("filePath");
	    String uploadIdApp = (String) execution.getVariable("appId");
	    String uploadToscaName = (String) execution.getVariable("toscaname");
	    String uploadProcessId = (String) execution.getVariable("processId");
	    Organizations org = (Organizations) execution.getVariable("org");
	    User user = (User) execution.getVariable("user");
		log.debug("Artifact UPLOAD Name: "+uploadName);
		File fileToDelete = FileUtils.getFile(uploadFilePath);
	    try {
		    
		    	String 	localFileAbsolutePath 	= fileToDelete.getAbsolutePath(),
						localFilePath = localFileAbsolutePath.substring(0,localFileAbsolutePath.lastIndexOf(File.separator)+1),
						localFileName 	= fileToDelete.getName(), 
						remoteFilePath 	= storeService.getTemplatePath(org.getOrganizationKey(),uploadToscaName), 
						remoteFileName	= fileToDelete.getName(),
						remoteFileNameReduced	= remoteFileName.substring(0,remoteFileName.indexOf(BpmnService.TEMP_FILE_NAME_SEPARATOR));
				log.debug("--- Local File Absolute Path:"+localFileAbsolutePath);
				log.debug("--- Local File Path:"+localFilePath);
				log.debug("--- Local File Name:"+localFileName);
				log.debug("--- Remote File Path:"+remoteFilePath);
				log.debug("--- Remote File Name:"+remoteFileNameReduced);
				storeService.storeFile(
						localFilePath, 
						localFileName, 
						remoteFilePath, 
						remoteFileNameReduced
				);
				log.debug("Artifact UPLOAD performed");
				//Add entry in referring persistent here
				this.addArtifactPath(execution, org.getOrganizationKey(), uploadToscaName, remoteFileNameReduced);
		} catch (eu.cloudopting.exceptions.StorageGeneralException e) {
			log.error("Error in storing Artifact File");
			e.printStackTrace();
		} finally {
			FileUtils.deleteQuietly(fileToDelete);
		}
	}

}
