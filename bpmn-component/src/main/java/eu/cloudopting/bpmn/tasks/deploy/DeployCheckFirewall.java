package eu.cloudopting.bpmn.tasks.deploy;

import java.util.concurrent.TimeUnit;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployCheckFirewall implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckFirewall.class);
	@Autowired
	CloudService cloudService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployCheckFirewall");
		String portForwardJobId = (String) execution.getVariable("portForwardJobId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		TimeUnit.SECONDS.sleep(4);
		boolean check = cloudService.checkFirewall(cloudAccountId, portForwardJobId);

		execution.setVariable("chkFirewall", check);
		
	}

}
