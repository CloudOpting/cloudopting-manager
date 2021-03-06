package eu.cloudopting.bpmn.tasks.publish.update;

import java.util.Map;

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
public class UpdateMetadata implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(UpdateMetadata.class);
	@Autowired
	ApplicationService applicationService;
	@Autowired
    private RuntimeService runtimeService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Update - Metadata Update");
		ApplicationDTO applicationData = (ApplicationDTO) execution.getVariable("application");
		Applications application = applicationService.findOne(applicationData.getId());
		BeanUtils.copyProperties(applicationData,application);
        applicationService.update(application);
        
        Map<String, Object> processVars = execution.getVariables();
	    processVars.put("applicationmetadataupdatedsuccess", true);
	    runtimeService.setVariables(execution.getProcessInstanceId(), processVars);
	}

}
