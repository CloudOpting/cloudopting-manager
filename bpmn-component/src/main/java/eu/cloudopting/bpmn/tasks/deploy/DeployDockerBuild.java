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
public class DeployDockerBuild implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDockerBuild.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	DockerService dockerService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployDockerBuild");
		String customizationId = (String) execution.getVariable("customizationId");
		String organizationName = (String) execution.getVariable("organizationName");
		String dockerNode = (String) execution.getVariable("dockerNode");
		String serviceHome = (String) execution.getVariable("serviceHome");
		String coRoot = (String) execution.getVariable("coRoot");
				
		
		String imageName = "cloudopting/"+organizationName+"_"+dockerNode.toLowerCase();
		String dockerFile = serviceHome + "/" + dockerNode + ".dockerfile";
		dockerService.buildDockerImage(imageName, dockerFile, coRoot);
//		toscaService.getNodeType("");
		//buildDockerImage(String customer, String service, String dockerfile,String path){
		//commands.add("docker build -t cloudopting/"+customer+"_"+dockerfile.toLowerCase()+" -f "+path+"/"+customer+"-"+service+"/"+dockerfile+".dockerfile "+path);
	    
		
	}

}
