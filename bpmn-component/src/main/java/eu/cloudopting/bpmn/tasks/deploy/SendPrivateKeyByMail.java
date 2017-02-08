package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.service.MailService;

@Service
public class SendPrivateKeyByMail implements JavaDelegate{
	private final Logger log = LoggerFactory.getLogger(DeploySetup.class);
	
	@Autowired
	MailService mailService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String privateKeyPath = (String)execution.getVariable("privateKeyPath");
		
		String to = "gioppo@csi.it";  //the email of the user of the organization that has bought the service
		mailService.sendPrivateKeyEmail(to, privateKeyPath);
		
		//the private key must be deleted after the VM has joined the Swarm
		
	}

}
