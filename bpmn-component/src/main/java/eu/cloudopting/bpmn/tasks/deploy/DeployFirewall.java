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
public class DeployFirewall implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployFirewall.class);
	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.debug("in DeployFirewall");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		String firewallJobId = "";
		if(this.doDeploy){
			firewallJobId = cloudService.createFirewall(cloudAccountId);
		}
		execution.setVariable("firewallJobId", firewallJobId);
		
	}

}
