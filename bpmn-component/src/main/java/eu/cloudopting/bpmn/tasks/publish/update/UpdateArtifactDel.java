package eu.cloudopting.bpmn.tasks.publish.update;

import java.util.Map;
import java.util.Set;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.ApplicationMedia;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.service.ApplicationMediaService;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.store.StoreService;

@Service
public class UpdateArtifactDel implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(UpdateArtifactDel.class);
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
		log.info("Update - Service Update Task - delete media");
		
		Long id = (Long)execution.getVariable("applicationid");
		Long idMedia = (Long)execution.getVariable("mediaid");
		
		Applications application = applicationService.findOne(id);
	    ApplicationMedia media = mediaService.findOne(idMedia);
	    
	    String filePath = media.getMediaContent();
	    
	    Set<ApplicationMedia> applicationMedias = application.getApplicationMedias();
	    applicationMedias.remove(media);
	    mediaService.delete(media.getId());
	    
	    String jrHttp = storeService.getJrHttp();
	    filePath = "/" + filePath.replaceAll(jrHttp, "");
		storeService.deleteFile(filePath);
	    
	    application.setApplicationMedias(applicationMedias);
	    
	    applicationService.update(application);
	    		
		Map<String, Object> processVars = execution.getVariables();
	    processVars.put("deletemediafilesuccess", true);
	    runtimeService.setVariables(execution.getProcessInstanceId(), processVars);
	}

}
