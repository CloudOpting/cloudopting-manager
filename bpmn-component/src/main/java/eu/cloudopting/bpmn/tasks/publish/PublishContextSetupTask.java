package eu.cloudopting.bpmn.tasks.publish;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PublishContextSetupTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishContextSetupTask.class);
//	@Autowired
//	ToscaService toscaService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Publish - Context SetUp");
		
		//execution.setVariable("isToscaPackageValid", isValid);
	}

}
