package eu.cloudopting.bpmn.tasks.publish.update;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import eu.cloudopting.exception.ToscaException;
import eu.cloudopting.service.ApplicationMediaService;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.store.StoreService;

@Service
public class UpdateArtifactAdd implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(UpdateArtifactAdd.class);
	@Autowired
	ApplicationService applicationService;
	@Autowired
	ApplicationMediaService mediaService;
	@Autowired
    private RuntimeService runtimeService;
	@Autowired(required = true)
	StoreService storeService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Update - Service Update Task - add media file");
		log.info("Update - Service Update Task");
		String uploadName = (String) execution.getVariable("name");
	    String uploadFilePath = (String) execution.getVariable("mediafilepath");
	    
	    Long id = (Long)execution.getVariable("applicationid");
	    Applications application = applicationService.findOne(id);
	    String uploadToscaName = application.getApplicationToscaName();
	    
	    
	    
	    Organizations org = (Organizations) execution.getVariable("org");
		log.debug("Media file UPLOAD Name: "+uploadName);
		
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
				log.debug("mediafile UPLOAD performed");
				//Add entry in referring persistent here
				updateMedia(application, org.getOrganizationKey(), uploadToscaName, remoteFileNameReduced);
				
				
				 Map<String, Object> processVars = execution.getVariables();
			     processVars.put("addmediafilesuccess", true);
			     runtimeService.setVariables(execution.getProcessInstanceId(), processVars);
		} catch (eu.cloudopting.exceptions.StorageGeneralException e) {
			log.error("Error in storing media file");
			e.printStackTrace();
		} catch (ToscaException e) {
			throw e;
		} finally {
			FileUtils.deleteQuietly(fileToDelete);
		}
	}
	
	private void updateMedia(Applications application, String orgKey, String toscaName,
			String remoteFileNameReduced) {
		Set<ApplicationMedia> medias = application.getApplicationMedias();
		String path = storeService.getTemplatePath(orgKey,toscaName, true)+"/"+remoteFileNameReduced;
		log.debug("templatepath from storeservice: " + path);
		
		log.debug("Actual size of medias: " + medias.size());
		
		if (medias.size() == 1) {
			log.debug("Update existing media");
			Object[] mediasList = medias.toArray();
			ApplicationMedia m = (ApplicationMedia)mediasList[0];
			String oldPath = m.getMediaContent();
			m.setMediaContent(path);
			
			//remove old file from jackrabbit
			
			String jrHttp = storeService.getJrHttp();
			String removePath = "/" + oldPath.replaceAll(jrHttp, "");
			log.debug("Oldpath: " + oldPath);
			log.debug("old jackrabbit path to remove: " + removePath);
			storeService.deleteFile(removePath);
			
			mediaService.update(m);
		}
		else if (medias.size() == 0) {
			log.debug("create new media");
			ApplicationMedia newMedia = new ApplicationMedia();
			newMedia.setApplicationId(application);
			newMedia.setMediaContent(path);
			mediaService.create(newMedia);
			
			medias.add(newMedia);
			
			application.setApplicationMedias(medias);
			applicationService.update(application);
		}
	}

}
