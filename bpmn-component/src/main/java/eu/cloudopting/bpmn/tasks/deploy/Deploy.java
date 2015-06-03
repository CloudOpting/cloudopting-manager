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
public class Deploy implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(Deploy.class);
	@Autowired
	DockerService dockerService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in Deploy");
		String customizationId = (String) execution.getVariable("customizationId");
		String serviceHome = (String) execution.getVariable("serviceHome");
		String composerFile = serviceHome + "/docker-compose.yml";
//		toscaService.getNodeType(customizationId,"");
		String deployToken = dockerService.deployComposition(composerFile, null);
		
//		"cd "+path+"/"+customer+"-"+service+" && docker-compose up --no-build -d"
		execution.setVariable("deployToken", deployToken);
	}

}
