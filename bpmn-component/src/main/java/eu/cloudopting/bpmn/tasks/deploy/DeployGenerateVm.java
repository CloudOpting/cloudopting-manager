package eu.cloudopting.bpmn.tasks.deploy;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployGenerateVm implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployGenerateVm.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	CloudService cloudService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployGenerateVm");
		String customizationId = (String) execution.getVariable("customizationId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		HashMap<String, String> data = toscaService.getCloudData(customizationId);
		String cloudtask = cloudService.createVM(cloudAccountId, data.get("cpu"), data.get("memory"), data.get("disk"));
		execution.setVariable("cloudtask", cloudtask);
		
	}

}
