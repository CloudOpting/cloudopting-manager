package eu.cloudopting.bpmn.tasks.publish;

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
import eu.cloudopting.tosca.ToscaService;

@Service
public class PublishToscaUploadTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishToscaUploadTask.class);

	@Autowired(required = true)
	ToscaService toscaService;

	@Autowired(required = true)
	StoreService storeService;
	
	@Inject
	private ApplicationService applicationService;
	
	@Autowired
    private RuntimeService runtimeService;
	
	/**
	 * Sets the "Application Tosca Template" attribute on an Application entity
	 * according to the format for JackRabbit
	 * @param execution
	 * @param orgKey
	 * @param toscaName
	 * @param remoteFileNameReduced
	 */
	private void setToscaTemplatePath(DelegateExecution execution, String orgKey, String toscaName, String remoteFileNameReduced){
		ApplicationDTO applicationSource = (ApplicationDTO) execution.getVariable("application");
        Applications application = applicationService.findOne(applicationSource.getId());
        application.setApplicationToscaTemplate(storeService.getTemplatePath(orgKey,toscaName,true)+"/"+remoteFileNameReduced);
        applicationService.update(application);
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Publish - Tosca File Upload");
		String uploadFilePath = (String) execution.getVariable("filePath");
		String uploadToscaName = (String) execution.getVariable("toscaname");
		Organizations org = (Organizations) execution.getVariable("org");

		File fileToDelete = FileUtils.getFile(uploadFilePath);
		//TODO check the result of upload to JackRabbit
		execution.setVariable("chkPublishToscaAvailable", true);
		try {
			boolean isValidToscaArchive = toscaService.validateToscaCsar(uploadFilePath);
			if (isValidToscaArchive) {
				log.debug("Valid Tosca Archive!");
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
					log.debug("Tosca Archive UPLOAD performed");
					this.setToscaTemplatePath(execution, org.getOrganizationKey(), uploadToscaName, remoteFileNameReduced);
				} catch (eu.cloudopting.exceptions.StorageGeneralException e) {
					log.error("Error in storing Tosca File");
					e.printStackTrace();
				}
				execution.setVariable("chkPublishToscaAvailable", true);
			} else {
				log.debug("Invalid Tosca Archive!");
			};
			
		} catch (ToscaException e) {
			throw e;
		} finally {
			FileUtils.deleteQuietly(fileToDelete);
		}
	}
}
