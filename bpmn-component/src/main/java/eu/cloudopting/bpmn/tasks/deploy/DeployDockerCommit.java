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
public class DeployDockerCommit implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDockerCommit.class);
	@Autowired
	DockerService dockerService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployDockerCommit");
		String imageName = (String) execution.getVariable("imageName");
//		toscaService.getNodeType("");
		
		String commitToken = dockerService.commitImage(imageName);
		
		execution.setVariable("commitToken", commitToken);
		
	}

}
