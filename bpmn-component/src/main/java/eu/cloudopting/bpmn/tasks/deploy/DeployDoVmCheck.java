package eu.cloudopting.bpmn.tasks.deploy;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployDoVmCheck implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDoVmCheck.class);
	@Autowired
	ToscaService toscaService;

	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployDoVmCheck");
		String customizationId = (String) execution.getVariable("customizationId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		HashMap<String, String> data = toscaService.getCloudData(customizationId);
		
		String cloudtask = (String) execution.getVariable("cloudtask");
		boolean created = cloudService.checkVM(-1l, cloudtask);
		log.debug("VM created: " + created);

		execution.setVariable("checkDOCreated", created);
		

	}

}
