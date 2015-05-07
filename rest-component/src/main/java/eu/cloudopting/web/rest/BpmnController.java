package eu.cloudopting.web.rest;

import javax.annotation.security.RolesAllowed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.security.AuthoritiesConstants;
import eu.cloudopting.tosca.ToscaService;

/**
 * REST controller for managing tosca
 */
@RestController
@RequestMapping("/api")
public class BpmnController {
	private final Logger log = LoggerFactory.getLogger(BpmnController.class);

	@Autowired
	private BpmnService bpmn;
	
    @RequestMapping(value = "/bpmn/imageStatus/{id}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    @ResponseBody byte[] getToscaGraph(@PathVariable String id) {
        log.info("REST request to get graph of tosca id : {}", id);
        return bpmn.getProcessStatusImage(id);
    }
    
	@RequestMapping(value = "/process", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String startProcessInstance(
			@RequestParam(value = "customizationId", required = false) String customizationId,
			@RequestParam(value = "cloudId", required = false) String cloudId) {
		String pid = bpmn.startDeployProcess(customizationId, cloudId);
		System.out.println("returning pid: " + pid);
		return pid;
	}

}
