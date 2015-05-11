package eu.cloudopting.bpmn.tasks.deploy;

import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;

@Service
public class DeploySetup implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeploySetup.class);
	@Autowired
	ToscaService toscaService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.info("in DeploySetup");
		System.out.println("in setup");
//		toscaService.getNodeType("");
		ArrayList<String> dockerPortsList = new ArrayList<String>();
		dockerPortsList.add("Port1");
		execution.setVariable("dockerNodesList", dockerPortsList);
		execution.setVariable("vmPortsList", dockerPortsList);
		
		
		// setting the variables for the rest of the tasks
		execution.setVariable("creationPath", null);
		execution.setVariable("dockerContextPath", null);
		execution.setVariable("service", null);
		execution.setVariable("servicePath", null);

		
	}

}
