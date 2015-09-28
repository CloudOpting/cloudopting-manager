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
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployCheckVm implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckVm.class);
	@Autowired
	ToscaService toscaService;

	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployCheckVm");
		String cloudtask = (String) execution.getVariable("cloudtask");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		if (this.doDeploy) {
			TimeUnit.SECONDS.sleep(25);

			boolean check = cloudService.checkVM(cloudAccountId, cloudtask);
			if (check) {
				JSONObject vmInfo = cloudService.getVMinfo(cloudAccountId, cloudtask);
				execution.setVariable("vmId", vmInfo.get("vmId"));
			}
			execution.setVariable("vmInstalled", check);
		} else {
			execution.setVariable("vmInstalled", true);

		}

	}

}
