package eu.cloudopting.bpmn.tasks.publish;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SimpleServiceTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(SimpleServiceTask.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.debug("I'm executing the Simple Service Task! YEAH!");
		//execution.setVariable("simpleServiceTaskExecuted", true);
	}

}
