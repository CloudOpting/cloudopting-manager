package eu.cloudopting.bpmn.tasks.deploy;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;
import eu.cloudopting.tosca.nodes.CloudOptingNodeImpl;

@Service
public class DeployGenerateDocker implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployGenerateDocker.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	CloudOptingNodeImpl cloudOptingNodeImpl;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployGenerateDocker");
		String customizationId = (String) execution.getVariable("customizationId");
		String dockerNode = (String) execution.getVariable("dockerNode");
		String coRoot = (String) execution.getVariable("coRoot");
		String serviceHome = (String) execution.getVariable("serviceHome");
		String organizationName = (String) execution.getVariable("organizationName");
//		toscaService.getNodeType("");
//		CloudOptingNodeImpl dc = new CloudOptingNodeImpl();
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("id", dockerNode);
		hm.put("creationPath", coRoot);
		hm.put("servicePath", serviceHome);
		hm.put("toscaPath", serviceHome+"/tosca/");
		hm.put("customer", organizationName);
		hm.put("customizationId", customizationId);
		cloudOptingNodeImpl.execute(hm);
		
	}

}
