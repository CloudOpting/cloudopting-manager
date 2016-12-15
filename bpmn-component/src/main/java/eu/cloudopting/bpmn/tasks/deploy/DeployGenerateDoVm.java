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
public class DeployGenerateDoVm implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployGenerateDoVm.class);
	@Autowired
	ToscaService toscaService;

	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployGenerateDoVm");
		String customizationId = (String) execution.getVariable("customizationId");
		String publickey = (String) execution.getVariable("publickey");
		log.debug("customizationId " + customizationId);
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		log.debug("cloudAccountId " + cloudAccountId);
		HashMap<String, String> data = toscaService.getCloudData(customizationId);
		
		log.debug("cloudData hashmap:");
		for(String k : data.keySet()) {
			log.debug(k + ": " + data.get(k));
		}
		//TODO for Davide save privatekey file
		data.put("publickey", publickey);
		String cloudtask = "";
		if (this.doDeploy) {
			cloudtask = cloudService.createVM(cloudAccountId, data,execution.getProcessInstanceId());
		log.debug("Cloudtask content:  " + cloudtask);
		}

		execution.setVariable("cloudtask", cloudtask);

	}

}
