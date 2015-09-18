package eu.cloudopting.bpmn.tasks.publish;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.User;
import eu.cloudopting.store.StoreService;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;

@Service
public class PublishToscaUploadTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishToscaUploadTask.class);
	@Autowired(required=true)
	StoreService storeService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Publish - Tosca File Upload");
		byte[] uploadFileBytes = (byte[])execution.getVariable("fileBytes");
		String uploadName = (String) execution.getVariable("name");
	    String uploadType = (String) execution.getVariable("type");
	    String uploadFileId = (String) execution.getVariable("fileId");
	    String uploadIdApp = (String) execution.getVariable("appId");
	    String uploadProcessId = (String) execution.getVariable("processId");
	    Organizations org = (Organizations) execution.getVariable("org");
	    User user = (User) execution.getVariable("user");
		log.debug("TOSCA UPLOAD Name: "+uploadName);
		InputStream is = new ByteArrayInputStream(uploadFileBytes);
		JackrabbitStoreRequest jrReq = new JackrabbitStoreRequest(storeService.getTemplatePath(org.getOrganizationKey(),  uploadIdApp), uploadName, new Date(), uploadName.substring(uploadName.lastIndexOf(".")+1), is);
		JackrabbitStoreResult res = storeService.storeBinary(jrReq);
		log.debug("TOSCA UPLOAD Successfull? "+res.isStored());
	}

}
