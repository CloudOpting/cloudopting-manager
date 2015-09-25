package eu.cloudopting.bpmn.tasks.publish;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Status;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.StatusService;
import eu.cloudopting.service.util.StatusConstants;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Service
@Transactional
public class PublishContextSetupTask implements JavaDelegate {
    private final Logger log = LoggerFactory.getLogger(PublishContextSetupTask.class);
    //	@Autowired
//	ToscaService toscaService;
    @Inject
    private StatusService statusService;

    @Inject
    private ApplicationService applicationService;



    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Publish - Context SetUp");
        ApplicationDTO applicationSource = (ApplicationDTO) execution.getVariable("application");
        Status status = statusService.findOne(StatusConstants.DRAFT);
        applicationSource.setStatus(status.getStatus());
        Applications application = new Applications();
        BeanUtils.copyProperties(applicationSource,application);
        application.setStatusId(status);
        application.setApplicationVersion(String.valueOf(1));
        Applications savedApplication = applicationService.create(application);
        //UPDATE THE PROCESS VARIABLE
        applicationSource.setId(savedApplication.getId());
        applicationSource.setProcessId(execution.getProcessInstanceId());
        execution.setVariable("application", applicationSource);
        execution.setVariable("applicationId", savedApplication.getId());
        execution.setVariable("chkPublishMetadataAvailable", true);
    }

}
