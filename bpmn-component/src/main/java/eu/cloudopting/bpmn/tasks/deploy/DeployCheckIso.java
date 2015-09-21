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
public class DeployCheckIso implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployCheckIso.class);
	@Autowired
	CloudService cloudService;

	@Value("${cloud.doDeploy}")
	private boolean doDeploy;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.debug("in DeployCheckIso");
		String isoTaskId = (String) execution.getVariable("isoTaskId");
		Long cloudAccountId = (Long) execution.getVariable("cloudAccountId");
		if (this.doDeploy) {
			TimeUnit.SECONDS.sleep(4);
			boolean check = cloudService.checkIso(cloudAccountId, isoTaskId);

			execution.setVariable("chkIso", check);
		} else {
			execution.setVariable("chkIso", true);
		}

	}

}
