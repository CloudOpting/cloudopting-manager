package eu.cloudopting.bpmn.tasks.publish.update;

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
public class UpdateToscaFile implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(UpdateToscaFile.class);
	@Autowired
	ApplicationService applicationService;
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Update - Service Update Task");
//		ApplicationDTO applications = (ApplicationDTO) execution.getVariable("application");
//		Applications application = applicationService.findOne(applications.getId());
//		//Perform all the additional publishing logic here...
//		//... then
//		BeanUtils.copyProperties(applications,application);
//        applicationService.update(application);
//        execution.setVariable("chkPublishMetadataAvailable", true);
	}

}
