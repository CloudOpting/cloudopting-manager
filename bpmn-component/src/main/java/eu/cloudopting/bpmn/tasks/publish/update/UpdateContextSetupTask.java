package eu.cloudopting.bpmn.tasks.publish.update;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.service.ApplicationService;

@Service
@Transactional
public class UpdateContextSetupTask implements JavaDelegate {
    private final Logger log = LoggerFactory.getLogger(UpdateContextSetupTask.class);
    @Inject
    private ApplicationService applicationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Update - Context SetUp");
        Long applicationId = (Long) execution.getVariable("applicationId");
        Applications application = applicationService.findOne(applicationId);
        //UPDATE/SET THE PROCESS VARIABLES
        execution.setVariable("chkPublishMetadataAvailable", true);
    }

}
