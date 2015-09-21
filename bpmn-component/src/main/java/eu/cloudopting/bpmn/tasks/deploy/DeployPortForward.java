package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.cloud.CloudService;

@Service
public class DeployPortForward implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployPortForward.class);
	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployPortForward");
		String vmId = (String) execution.getVariable("vmId");
		String ipId = (String) execution.getVariable("ipId");
		String vmPort = (String) execution.getVariable("vmPort");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		log.debug(vmId);
		String portForwardJobId = "";
		if (this.doDeploy) {
			portForwardJobId = cloudService.createPortForward(cloudAccountId, ipId, vmId, Integer.parseInt(vmPort),
					Integer.parseInt(vmPort));
		}

		execution.setVariable("portForwardJobId", portForwardJobId);

	}

}
