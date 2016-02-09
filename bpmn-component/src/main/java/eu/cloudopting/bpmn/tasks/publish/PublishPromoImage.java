package eu.cloudopting.bpmn.tasks.publish;
/**
*
* @author Claudio
*/
import java.io.File;

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
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.exception.ToscaException;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.store.StoreService;

@Service
public class PublishPromoImage implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishPromoImage.class);

	@Autowired(required = true)
	StoreService storeService;
	
	@Autowired
    private RuntimeService runtimeService;
	
	@Inject
	private ApplicationService applicationService;
	
	/**
	 * Adds the Promo Image (a.k.a. Logo) of the Application
	 * according to the format for JackRabbit
	 * @param execution
	 * @param orgKey
	 * @param toscaName
	 * @param remoteFileNameReduced
	 */
	private void addPromoImagePath(DelegateExecution execution, String orgKey, String toscaName, String remoteFileNameReduced){
		ApplicationDTO applicationSource = (ApplicationDTO) execution.getVariable("application");
        Applications application = applicationService.findOne(applicationSource.getId());
        application.setApplicationLogoReference(StoreService.getTemplatePath(orgKey,toscaName)+"/"+remoteFileNameReduced);
        applicationService.update(application);
	}
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Upload - Promo Image");
		String uploadName = (String) execution.getVariable("name");
	    String uploadFilePath = (String) execution.getVariable("filePath");
	    String uploadToscaName = (String) execution.getVariable("toscaname");
	    Organizations org = (Organizations) execution.getVariable("org");
		log.debug("Promo Image UPLOAD Name: "+uploadName);
		File fileToDelete = FileUtils.getFile(uploadFilePath);
	    try {
		    
		    	String 	localFileAbsolutePath 	= fileToDelete.getAbsolutePath(),
						localFilePath = localFileAbsolutePath.substring(0,localFileAbsolutePath.lastIndexOf(File.separator)+1),
						localFileName 	= fileToDelete.getName(), 
						remoteFilePath 	= StoreService.getTemplatePath(org.getOrganizationKey(),uploadToscaName), 
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
				this.addPromoImagePath(execution, org.getOrganizationKey(), uploadToscaName, remoteFileNameReduced);
		} catch (eu.cloudopting.exceptions.StorageGeneralException e) {
			log.error("Error in storing promoImage");
			e.printStackTrace();
		} catch (ToscaException e) {
			throw e;
		} finally {
			FileUtils.deleteQuietly(fileToDelete);
		}
	}
}
