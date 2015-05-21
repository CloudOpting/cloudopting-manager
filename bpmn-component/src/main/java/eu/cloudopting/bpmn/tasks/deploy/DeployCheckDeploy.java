package eu.cloudopting.bpmn.tasks.deploy;

import java.util.concurrent.TimeUnit;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.docker.DockerService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployCheckDeploy implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckDeploy.class);
	@Autowired
	DockerService dockerService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployCheckDeploy");
		String customizationId = (String) execution.getVariable("customizationId");
		String deployToken = (String) execution.getVariable("deployToken");
//		toscaService.getNodeType(customizationId,"");
		TimeUnit.SECONDS.sleep(4);
		boolean check = dockerService.isCompositionDeployed(deployToken);
		execution.setVariable("chkDeploy", check);
	}

}
