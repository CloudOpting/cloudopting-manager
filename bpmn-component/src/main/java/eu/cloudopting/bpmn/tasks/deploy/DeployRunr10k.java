package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.docker.DockerService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployRunr10k implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployRunr10k.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	DockerService dockerService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployRunr10k");
		String customizationId = (String) execution.getVariable("customizationId");
		String coRoot = (String) execution.getVariable("coRoot");
		String serviceHome = (String) execution.getVariable("serviceHome");
		toscaService.runR10k(customizationId, serviceHome, coRoot);
		String dockerContext = dockerService.newContext(serviceHome + "/Puppetfile");
		execution.setVariable("dockerContext", dockerContext);
	}

}
