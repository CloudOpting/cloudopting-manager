package eu.cloudopting.bpmn.tasks.publish;

import java.io.File;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.User;
import eu.cloudopting.exception.ToscaException;
import eu.cloudopting.store.StoreService;

@Service
public class PublishArtifactStorageTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishArtifactStorageTask.class);
	@Autowired(required=true)
	StoreService storeService;

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
//	    InputStream in = new FileInputStream(fileToDelete);
	    //TODO Fix this when JackRabbit works
	    execution.setVariable("chkPublishArtifactsAvailable", true);
	    try {
//		    in = new java.io.BufferedInputStream(in);
//		    JackrabbitStoreRequest jrReq = 
//		    		new JackrabbitStoreRequest(storeService.getTemplatePath(org.getOrganizationKey(),  uploadIdApp), 
//		    				uploadName, new Date(), uploadName.substring(uploadName.lastIndexOf(".")+1), in);
		    try {
//				JackrabbitStoreResult res = storeService.storeBinary(jrReq);
		    	String 	localFileAbsolutePath 	= fileToDelete.getAbsolutePath(),
						localFilePath = localFileAbsolutePath.substring(0,localFileAbsolutePath.lastIndexOf(File.separator)+1),
						localFileName 	= fileToDelete.getName(), 
						remoteFilePath 	= storeService.getTemplatePath(org.getOrganizationKey(),uploadToscaName), 
						remoteFileName	= fileToDelete.getName(),
						remoteFileNameReduced	= remoteFileName.substring(0,remoteFileName.indexOf("|"));
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
			} catch (eu.cloudopting.exceptions.StorageGeneralException e) {
				// TODO Auto-generated catch block
				log.error("Error in storing Artifact File");
				e.printStackTrace();
			}
			execution.setVariable("chkPublishArtifactsAvailable", true);
	    } catch (ToscaException e) {
			throw e;
		} finally {
			FileUtils.deleteQuietly(fileToDelete);
//			IOUtils.closeQuietly(in);
		}
	}

}
