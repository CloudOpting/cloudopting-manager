package eu.cloudopting.bpmn.tasks.publish;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.store.StoreService;

@Service
public class DeleteServiceTask implements JavaDelegate {
	
	private final Logger log = LoggerFactory.getLogger(DeleteServiceTask.class);
	@Autowired
	ApplicationService applicationService;
	@Autowired(required = true)
	StoreService storeService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		ApplicationDTO app = (ApplicationDTO) execution.getVariable("applicationdto");
		log.debug("Deleting application with id:"+app.getId());
		//TODO: Implement clean-up of JackRabbit as soon as the Store Service API
		//      exposes a "Remove" method
		
		//TODO CHECK WHY THE APPLICATION DOES NOT HAVE AN ASSOCIATED ORGANIZATION
		//     AS SOON AS THE DATABASE COMES BACK UP
		Applications a = applicationService.findOne(app.getId());
		storeService.deletePath(a.getOrganizationId().getOrganizationKey(), a.getApplicationToscaName());
		applicationService.delete(app.getId());
		execution.setVariable("applicationdeletedid", app.getId());
	}

}
