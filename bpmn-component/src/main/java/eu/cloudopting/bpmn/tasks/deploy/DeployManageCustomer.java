package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.service.UserService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployManageCustomer implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployManageCustomer.class);
	@Autowired
	ToscaService toscaService;
	
	// @TODO here I will need the organization service
	@Autowired
	UserService userService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployManageCustomer");
		String organizationId = (String) execution.getVariable("organizationId");
		// @TODO here I will get the organization name and generate the proper string to use in path and so on
		execution.setVariable("organizationName", "csi");
//		toscaService.getNodeType("");
		
	}

}
