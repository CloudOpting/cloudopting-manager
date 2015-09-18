package eu.cloudopting.bpmn.tasks.publish;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.dto.UploadDTO;
import eu.cloudopting.storagecomponent.StoreRequest;
import eu.cloudopting.store.StoreService;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;

@Service
public class PublishArtifactStorageTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishArtifactStorageTask.class);
	@Autowired(required=false)
	StoreService storeService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Publish - Artifacts Storage");
		byte[] uploadFileBytes = (byte[])execution.getVariable("fileBytes");
		String uploadName = (String) execution.getVariable("name");
	    String uploadType = (String) execution.getVariable("type");
	    String uploadFileId = (String) execution.getVariable("fileId");
	    String uploadIdApp = (String) execution.getVariable("appId");
	    String uploadProcessId = (String) execution.getVariable("processId");
		log.debug("UPLOAD Name: "+uploadName);
		InputStream is = new ByteArrayInputStream(uploadFileBytes);
		JackrabbitStoreRequest jrReq = new JackrabbitStoreRequest(uploadIdApp, uploadName, new Date(), uploadName.substring(uploadName.lastIndexOf(".")), is);
		JackrabbitStoreResult res = storeService.storeBinary(jrReq);
		log.debug("UPLOAD Successfull? "+res.isStored());
//		//TODO Define and use a less generic Class
//		Object candidateToscaPackage = execution.getVariable("candidateToscaPackage");
//		//TODO Agree on responsibilities and an API signature
//		boolean isValid = true;
//		//isValid = toscaService.isPackageValid(candidateToscaPackage);
//		execution.setVariable("isToscaPackageValid", isValid);
	}

}
