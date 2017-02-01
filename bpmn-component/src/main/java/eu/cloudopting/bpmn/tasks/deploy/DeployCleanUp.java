package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployCleanUp implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCleanUp.class);
	@Autowired
	ToscaService toscaService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployCleanUp");
		String customizationId = (String) execution.getVariable("customizationId");
//		toscaService.getNodeType(customizationId,"");
		// Remove the tosca customization
		toscaService.removeToscaCustomization(customizationId);
		// delete the folder
		
		// remove the caches in dockerservice
		// TODO rimuovere le chiavi
		
	}

}
