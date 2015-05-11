package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.Status;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployManageCustomization implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployManageCustomization.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	CustomizationService cusomizationS;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in Deploy");
		String customizationId = (String) execution.getVariable("customizationId");
		Customizations theCust = cusomizationS.findOne(Long.parseLong(customizationId));
		log.info(theCust.toString());
		theCust.setStatusId(new Status().findStatus((long) 90));
		toscaService.getNodeType("");
		
	}

}
