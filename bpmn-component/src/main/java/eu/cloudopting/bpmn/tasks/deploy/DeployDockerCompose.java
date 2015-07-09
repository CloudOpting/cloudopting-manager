package eu.cloudopting.bpmn.tasks.deploy;

import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.docker.DockerService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployDockerCompose implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDockerCompose.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	DockerService dockerService;


	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployDockerCompose");
		String customizationId = (String) execution.getVariable("customizationId");
		String organizationName = (String) execution.getVariable("organizationName");
		String serviceHome = (String) execution.getVariable("serviceHome");
		ArrayList<String> dockerNodesList = (ArrayList<String>) execution.getVariable("dockerNodesList");
		toscaService.generateDockerCompose(customizationId, organizationName, serviceHome, dockerNodesList);
		dockerService.deployComposition(serviceHome+"docker-compose.yml");
	}

}
