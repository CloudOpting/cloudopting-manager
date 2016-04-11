package eu.cloudopting.bpmn.tasks.publish.update;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.service.ApplicationService;

@Service
public class UpdateMetadataTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(UpdateMetadataTask.class);
	@Autowired
	ApplicationService applicationService;
	@Autowired
    private RuntimeService runtimeService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Update - Metadata Update");
		ApplicationDTO applications = (ApplicationDTO) execution.getVariable("application");
		Applications application = applicationService.findOne(applications.getId());
		BeanUtils.copyProperties(applications,application);
        applicationService.update(application);
        execution.setVariable("toscaname", application.getApplicationToscaName());
        execution.setVariable("chkPublishMetadataAvailable", true);
	}

}
