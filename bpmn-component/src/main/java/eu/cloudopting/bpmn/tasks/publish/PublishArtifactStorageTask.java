package eu.cloudopting.bpmn.tasks.publish;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;

@Service
public class PublishArtifactStorageTask implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(PublishArtifactStorageTask.class);
	@Autowired(required=false)
	ToscaService toscaService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Publish - Artifacts Storage");
//		//TODO Define and use a less generic Class
//		Object candidateToscaPackage = execution.getVariable("candidateToscaPackage");
//		//TODO Agree on responsibilities and an API signature
//		boolean isValid = true;
//		//isValid = toscaService.isPackageValid(candidateToscaPackage);
//		execution.setVariable("isToscaPackageValid", isValid);
	}

}
