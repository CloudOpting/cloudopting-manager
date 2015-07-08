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
		String dockerContext = (String) execution.getVariable("dockerContext");
				
		log.debug("dockerContext: "+dockerContext);
		
		String imageName = organizationName+"_"+dockerNode.toLowerCase();
		String dockerFile = serviceHome + "/" + dockerNode + ".dockerfile";
		String puppetManifest = serviceHome + "/" + dockerNode + ".pp";
		log.debug("imageName: "+imageName);
		log.debug("dockerFile: "+dockerFile);
		log.debug("puppetManifest: "+puppetManifest);

		String buildToken = dockerService.buildDockerImage(imageName, dockerFile, puppetManifest, dockerContext);
//		toscaService.getNodeType("");
		//buildDockerImage(String customer, String service, String dockerfile,String path){
		//commands.add("docker build -t cloudopting/"+customer+"_"+dockerfile.toLowerCase()+" -f "+path+"/"+customer+"-"+service+"/"+dockerfile+".dockerfile "+path);
	    
		execution.setVariable("buildToken", buildToken);
		execution.setVariable("imageName", imageName);
	}

}
