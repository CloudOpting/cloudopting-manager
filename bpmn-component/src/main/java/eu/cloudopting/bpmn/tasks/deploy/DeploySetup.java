package eu.cloudopting.bpmn.tasks.deploy;

import java.io.File;
import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;

@Service
public class DeploySetup implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeploySetup.class);
	@Autowired
	ToscaService toscaService;
	
	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.info("in DeploySetup");
		String customizationId = (String) execution.getVariable("customizationId");
		String organizationName = (String) execution.getVariable("organizationName");
		String service = toscaService.getServiceName(customizationId);
		log.debug("service: "+service);
		String coRoot = new String("/cloudOptingData");
		String serviceHome = new String(coRoot+"/"+organizationName+"-"+service);
		log.debug("serviceHome: "+serviceHome);
		
		boolean success = false;
		File directory = new File(serviceHome);
		if (directory.exists()) {
			System.out.println("Directory already exists ...");

		} else {
			System.out.println("Directory not exists, creating now");

			success = directory.mkdir();
			if (success) {
				System.out.printf("Successfully created new directory : %s%n",
						serviceHome);
			} else {
				System.out.printf("Failed to create new directory: %s%n", serviceHome);
			}
		}
		
		toscaService.generatePuppetfile(customizationId, serviceHome);
		
		ArrayList<String> dockerPortsList = toscaService.getHostPorts(customizationId);
		dockerPortsList.add("Port1");
		
		ArrayList<String> dockerNodesList = toscaService.getArrNodesByType(customizationId, "DockerContainer");
		log.debug(dockerNodesList.toString());
		execution.setVariable("dockerNodesList", dockerNodesList);
		execution.setVariable("vmPortsList", dockerPortsList);
		
		
		// setting the variables for the rest of the tasks
		execution.setVariable("customizationName", organizationName+"-"+service);
		execution.setVariable("coRoot", coRoot);
		execution.setVariable("service", service);
		execution.setVariable("serviceHome", serviceHome);

		
	}

}
