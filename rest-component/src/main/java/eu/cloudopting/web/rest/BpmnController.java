package eu.cloudopting.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.bpmn.dto.BasicProcessInfo;
import eu.cloudopting.domain.User;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.security.AuthoritiesConstants;
import eu.cloudopting.service.UserService;


/**
 * REST controller for managing tosca
 */
@RestController
@RequestMapping("/api")
public class BpmnController {
	private final Logger log = LoggerFactory.getLogger(BpmnController.class);

    @Inject
    UserService userService;


    public UserService getUserService() {
        return userService;
    }

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
			@RequestParam(value = "cloudId", required = false) String cloudId, HttpServletRequest request) {

        User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
        user.getOrganizationId().getOrganizationKey();
		String pid = bpmn.startDeployProcess(customizationId, cloudId);
		System.out.println("returning pid: " + pid);
		return pid;
	}
	
	@RequestMapping(value = "/bpmn/configuredVM/{processInstanceId}", method = RequestMethod.POST)
	public @ResponseBody void configuredVM(@PathVariable String processInstanceId){
		bpmn.configuredVM(processInstanceId);
	}
	
	@RequestMapping(value = "/bpmn/startProcess/{id}",
            method = RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    @ResponseBody String startProcessById(@PathVariable String id) {
        log.info("REST request to start process with id : {}", id);
        return bpmn.startGenericProcess(id, null);
    }
	
	@RequestMapping(value = "/bpmn/availableProcessDefinitions",
            method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    @ResponseBody List<BasicProcessInfo> getAvailableProcessDefinitions() {
        log.info("REST request to get processdefinitions");
        return bpmn.getAvailableProcessDefinitions();
    }
	
	@RequestMapping(value = "/bpmn/deleteProcessDeployment",
            method = RequestMethod.GET)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    @ResponseBody void deleteProcessDeployment(@RequestParam(value = "deploymentId", required = true) String deploymentId) {
        log.info("REST request to delete deployment by id");
        bpmn.deleteDeploymentById(deploymentId);;
    }
	
	@RequestMapping(value = "/bpmn/publish/updateMetadata/{processInstanceId}",
            method = RequestMethod.PUT)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    @ResponseBody Set<String> updateMetadata(@PathVariable String processInstanceId, @RequestBody ApplicationDTO application) {
        log.info("REST request to update metadata for process instance with id: {}, application {}", processInstanceId, application);
        Map<String, ApplicationDTO> params = new HashMap<String, ApplicationDTO>();
        params.put("application", application);
        //Return the updated value of the model
        return bpmn.unlockProcess(processInstanceId, "MetadataRetrievalEvent", params);
    }


}
