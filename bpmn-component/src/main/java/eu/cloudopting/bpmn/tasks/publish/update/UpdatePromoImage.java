package eu.cloudopting.bpmn.tasks.publish.update;

import java.io.File;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.exception.ToscaException;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.store.StoreService;

@Service
public class UpdatePromoImage implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(UpdatePromoImage.class);
	@Autowired
	ApplicationService applicationService;
	@Autowired(required = true)
	StoreService storeService;
	@Autowired
    private RuntimeService runtimeService;
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Update - Change Promo Image");
		//TODO: remove the existing image in JR
		//TODO: set the new image
		
		log.info("Upload - Promo Image");
		String uploadName = (String) execution.getVariable("name");
	    String uploadFilePath = (String) execution.getVariable("newlogopath");
	    
	    Long id = (Long)execution.getVariable("applicationid");
	    Applications application = applicationService.findOne(id);
	    String uploadToscaName = application.getApplicationToscaName();
	    
	    String oldLogoPath = application.getApplicationLogoReference();
	    
	    Organizations org = (Organizations) execution.getVariable("org");
		log.debug("Promo Image UPLOAD Name: "+uploadName);
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
				log.debug("promoImage UPLOAD performed");
				//Add entry in referring persistent here
				updateLogoImagePath(application, org.getOrganizationKey(), uploadToscaName, remoteFileNameReduced);
				
				String jrHttp = storeService.getJrHttp();
				oldLogoPath = "/" + oldLogoPath.replaceAll(jrHttp, "");
				storeService.deleteFile(oldLogoPath);
				
				 Map<String, Object> processVars = execution.getVariables();
			     processVars.put("applicationlogoupdatedsuccess", true);
			     runtimeService.setVariables(execution.getProcessInstanceId(), processVars);
		} catch (eu.cloudopting.exceptions.StorageGeneralException e) {
			log.error("Error in storing promoImage");
			e.printStackTrace();
		} catch (ToscaException e) {
			throw e;
		} finally {
			FileUtils.deleteQuietly(fileToDelete);
		}
	}


	private void updateLogoImagePath(Applications application, String orgKey, String toscaName,
			String remoteFileNameReduced) {
		String path = storeService.getTemplatePath(orgKey,toscaName, true)+"/"+remoteFileNameReduced;
		application.setApplicationLogoReference(path);
        applicationService.update(application);
		
	}

}
