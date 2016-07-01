package eu.cloudopting.bpmn.tasks.deploy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;

@Service
public class DeployCheckAcquireIp implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckAcquireIp.class);
	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.debug("in DeployCheckAcquireIp");
		Map<String, Object> cloudParams = constructCloudParams(execution);
		if (this.doDeploy) {
//			TimeUnit.SECONDS.sleep(20);
			boolean check = cloudService.checkAssociateIp(cloudParams);
			if (check) {
				JSONObject ipInfo = cloudService.getAssociatedIpinfo(cloudParams);
				execution.setVariable("ipId", ipInfo.get("ipId"));
				execution.setVariable("ip", ipInfo.get("ip"));
			}
			execution.setVariable("chkAcquiredIp", check);
		} else {
			execution.setVariable("chkAcquiredIp", true);
		}

		//
		// toscaService.getNodeType(customizationId,"");
		// Remove the tosca customization
		// toscaService.removeToscaCustomization(customizationId);
		// delete the folder

		// remove the caches in dockerservice
		boolean checkIp = true;
		execution.setVariable("chkAcquiredIp", checkIp);
	}

	private Map<String, Object> constructCloudParams(DelegateExecution execution){
		Map<String, Object> result = new HashMap<String, Object>();
		String acquireJobId = (String) execution.getVariable("acquireJobId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		String vmId = (String) execution.getVariable("vmId");
		
		result.put("acquireJobId", acquireJobId);
		result.put("cloudAccountId", cloudAccountId);
		result.put("vmId", vmId);
		return result;
	}
}
