package eu.cloudopting.bpmn.tasks.deploy;

import java.util.concurrent.TimeUnit;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployCheckAcquireIp implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckAcquireIp.class);
	@Autowired
	CloudService cloudService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployCheckAcquireIp");
		String customizationId = (String) execution.getVariable("customizationId");
		String acquireJobId = (String) execution.getVariable("acquireJobId");
		String cloudId = (String) execution.getVariable("cloudId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		TimeUnit.SECONDS.sleep(4);
		boolean check = cloudService.checkAssociateIp(cloudAccountId, acquireJobId);
		if(check){
			JSONObject ipInfo = cloudService.getAssociatedIpinfo(cloudAccountId, acquireJobId);
			execution.setVariable("ipId", ipInfo.get("ipId"));
			execution.setVariable("ip", ipInfo.get("ip"));
		}
		execution.setVariable("chkAcquiredIp", check);

		
		//		
//		toscaService.getNodeType(customizationId,"");
		// Remove the tosca customization
//		toscaService.removeToscaCustomization(customizationId);
		// delete the folder
		
		// remove the caches in dockerservice
		boolean checkIp = true;
		execution.setVariable("chkAcquiredIp", checkIp);
	}

}
