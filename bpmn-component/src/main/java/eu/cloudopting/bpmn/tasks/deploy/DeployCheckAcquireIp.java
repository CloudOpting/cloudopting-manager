package eu.cloudopting.bpmn.tasks.deploy;

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
		// TODO Auto-generated method stub
		log.debug("in DeployCheckAcquireIp");
		String acquireJobId = (String) execution.getVariable("acquireJobId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		if (this.doDeploy) {
			TimeUnit.SECONDS.sleep(4);
			boolean check = cloudService.checkAssociateIp(cloudAccountId, acquireJobId);
			if (check) {
				JSONObject ipInfo = cloudService.getAssociatedIpinfo(cloudAccountId, acquireJobId);
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

}
