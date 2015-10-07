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
public class DeployDockerSwarm implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDockerSwarm.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	DockerService dockerService;


	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String ip = (String) execution.getVariable("ip");

		log.debug("in DeployDockerSwarm");
//		toscaService.getNodeType("");
		dockerService.addMachine(ip, 2376);
		
	}

}
