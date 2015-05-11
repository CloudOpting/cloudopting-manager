package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployGeneratePuppet implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployGeneratePuppet.class);
	@Autowired
	ToscaService toscaService;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeploySetup");
//		toscaService.getNodeType("");
		
	}

}
