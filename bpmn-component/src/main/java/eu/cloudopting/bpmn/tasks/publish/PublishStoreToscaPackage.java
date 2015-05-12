package eu.cloudopting.bpmn.tasks.publish;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PublishStoreToscaPackage implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishStoreToscaPackage.class);
//	@Autowired
//	ToscaService toscaService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Storing the validated Tosca Package");
		
		//execution.setVariable("isToscaPackageValid", isValid);
	}

}
