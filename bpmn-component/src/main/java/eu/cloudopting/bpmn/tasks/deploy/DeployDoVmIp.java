package eu.cloudopting.bpmn.tasks.deploy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployDoVmIp implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDoVmIp.class);
	@Autowired
	ToscaService toscaService;

	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployDoVmIp");
		String customizationId = (String) execution.getVariable("customizationId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		HashMap<String, String> data = toscaService.getCloudData(customizationId);
		
		String cloudtask = (String) execution.getVariable("cloudtask");
		
		Map<String, Object> cloudParams = new HashMap<>();
		cloudParams.put("vmId", cloudtask);
		cloudParams.put("cloudAccountId", cloudAccountId);
		JSONObject result = cloudService.getAssociatedIpinfo(cloudParams);
		Iterator it = result.keys();
		while(it.hasNext()) {
			String key = (String)it.next();
			log.debug(key + ": " +result.get(key));
		}
		String ip = result.getString("ip");
		execution.setVariable("ip", ip);

	}

}
