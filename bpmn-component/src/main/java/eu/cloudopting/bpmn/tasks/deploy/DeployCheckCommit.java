package eu.cloudopting.bpmn.tasks.deploy;

import java.util.concurrent.TimeUnit;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.docker.DockerService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployCheckCommit implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckCommit.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	DockerService dockerService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployCheckCommit");
		String commitToken = (String) execution.getVariable("commitToken");
		TimeUnit.SECONDS.sleep(4);
//		toscaService.getNodeType(customizationId,"");
		boolean check = dockerService.isBuilt(commitToken);
		execution.setVariable("vmInstalled", check);
		
	}

}
