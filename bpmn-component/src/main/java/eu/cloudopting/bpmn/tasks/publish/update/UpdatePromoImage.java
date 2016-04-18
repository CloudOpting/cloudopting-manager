package eu.cloudopting.bpmn.tasks.publish.update;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.service.ApplicationService;

@Service
public class UpdatePromoImage implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(UpdatePromoImage.class);
	@Autowired
	ApplicationService applicationService;
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Update - Change Promo Image");
		//TODO: remove the existing image in JR
		//TODO: set the new image
	}

}
