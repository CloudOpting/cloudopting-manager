package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.store.StoreService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployManageTosca implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployManageTosca.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired(required = true)
	StoreService storeService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployManageTosca");
		String customizationId = (String) execution.getVariable("customizationId");
		String serviceHome = (String) execution.getVariable("serviceHome");
		String provider = (String) execution.getVariable("provider");
		String service = (String) execution.getVariable("service");
		String toscaCsarPath = (String) execution.getVariable("toscaCsarPath");
		// need to get the tosca csar from jackrabbit
		
		toscaService.manageToscaCsar(customizationId, service, serviceHome, provider, toscaCsarPath);
		
	}

}
