package eu.cloudopting.bpmn.tasks.publish;

import javax.inject.Inject;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Status;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.StatusService;
import eu.cloudopting.service.util.StatusConstants;

@Service
public class PublishPublishTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishPublishTask.class);
	@Autowired
	ApplicationService applicationService;
	@Inject
	private StatusService statusService;


	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Publish - Service Publish Task");
		ApplicationDTO applications = (ApplicationDTO) execution.getVariable("application");
		Applications application = applicationService.findOne(applications.getId());
		//Perform all the additional publishing logic here...
		//... then
		BeanUtils.copyProperties(applications,application);
        Status status = statusService.findOne(StatusConstants.PUBLISHED);
        application.setStatusId(status);
        applicationService.update(application);
        execution.setVariable("chkPublishMetadataAvailable", true);
	}

}
