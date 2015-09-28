package eu.cloudopting.bpmn.tasks.deploy;

import java.util.concurrent.TimeUnit;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;

@Service
public class DeployCheckPortForward implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckPortForward.class);
	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployCheckPortForward");
		String portForwardJobId = (String) execution.getVariable("portForwardJobId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		if (this.doDeploy) {
			TimeUnit.SECONDS.sleep(20);
			boolean check = cloudService.checkPortForward(cloudAccountId, portForwardJobId);
			execution.setVariable("chkForward", check);
		} else {
			execution.setVariable("chkForward", true);
		}

	}

}
