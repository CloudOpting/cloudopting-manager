package eu.cloudopting.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.bpmn.BpmnServiceConstants;
import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.User;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.security.AuthoritiesConstants;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.CustomizationService;
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

	@Inject
	CustomizationService customizationService;

	@Inject
	ApplicationService applicationService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	private BpmnService bpmn;

	@RequestMapping(value = "/bpmn/imageStatus/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	@RolesAllowed(AuthoritiesConstants.ANONYMOUS)
	@ResponseBody
	byte[] getToscaGraph(@PathVariable String id) {
		log.info("REST request to get graph of tosca id : {}", id);
		return bpmn.getProcessStatusImage(id);
	}

	@RequestMapping(value = "/process", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	public @ResponseBody String startProcessInstance(
			@RequestParam(value = "customizationId", required = false) String customizationId,
			@RequestParam(value = "isTesting", required = false, defaultValue = "false") boolean isTesting,
			@RequestParam(value = "isDemo", required = false, defaultValue = "false") boolean isDemo,
			HttpServletRequest request) {

		User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
		user.getOrganizationId().getOrganizationKey();
		Customizations customization = customizationService.findOne(new Long(customizationId));
		long cloudId = 0;
		// if is demo I choose the demo account of the service provider
		// overriding the one in the customization
		if (isDemo) {
			Set<CloudAccounts> spAccounts = customization.getApplicationId().getOrganizationId().getCloudAccountss();
			for (CloudAccounts acc : spAccounts) {
				// here need to do the check on default
				cloudId = acc.getId();
			}
		} else {
			cloudId = customization.getCloudAccount().getId();
		}
		String pid = bpmn.startDeployProcess(customizationId, cloudId, isTesting);
		System.out.println("returning pid: " + pid);
		return pid;
	}
	
	@RequestMapping(value = "/processTest", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	public void testProcessInstance(
			@RequestParam(value = "customizationId", required = false) String customizationId,
			@RequestParam(value = "isTesting", required = false, defaultValue = "true") boolean isTesting,
			@RequestParam(value = "isDemo", required = false, defaultValue = "false") boolean isDemo,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
		user.getOrganizationId().getOrganizationKey();
		Customizations customization = customizationService.findOne(new Long(customizationId));
		long cloudId = 0;
		// if is demo I choose the demo account of the service provider
		// overriding the one in the customization
		if (isDemo) {
			Set<CloudAccounts> spAccounts = customization.getApplicationId().getOrganizationId().getCloudAccountss();
			for (CloudAccounts acc : spAccounts) {
				// here need to do the check on default
				cloudId = acc.getId();
			}
		} else {
			cloudId = customization.getCloudAccount().getId();
		}
		String path = "/cloudOptingData/test.zip";
		File f = new File(path);
		if (f.exists()){
			f.delete();
		}
		String pid = bpmn.startDeployProcess(customizationId, cloudId, isTesting);
		// wait for a minute to leave run the process

		System.out.println("returning pid: " + pid);
		
		
		FileInputStream fis = null;
		while (!f.exists()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.debug("FILE DOES NOT EXIST YET **********************************************************");
		}

		fis = new FileInputStream(f);
		// ClassPathResource zipFile = new ClassPathResource(path);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Pragma", "no-cache");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Expires", "0");
		headers.add("Content-disposition", "attachment; filename=test.zip");

		response.addHeader("Content-disposition", "attachment; filename=test.zip");
		response.setContentType("application/zip");
		//response.setContentLength(FileUtils.sizeOf(f));

		IOUtils.copy(FileUtils.openInputStream(f), response.getOutputStream());
		response.flushBuffer();
/*
		return ResponseEntity.ok().headers(headers).contentLength(FileUtils.sizeOf(f))
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(new InputStreamResource(fis));
				*/
	}

	@RequestMapping(value = "/bpmnunlock/configuredVM/{processInstanceId}", method = RequestMethod.POST)
	public @ResponseBody void configuredVM(@PathVariable String processInstanceId) {
		bpmn.configuredVM(processInstanceId);
	}

	@RequestMapping(value = "/bpmn/deleteProcessDeployment", method = RequestMethod.GET)
	@RolesAllowed(AuthoritiesConstants.ANONYMOUS)
	@ResponseBody
	void deleteProcessDeployment(@RequestParam(value = "deploymentId", required = true) String deploymentId) {
		log.info("REST request to delete deployment by id");
		bpmn.deleteDeploymentById(deploymentId);
	}

	@RequestMapping(value = "/bpmn/publish/updateMetadata/{processInstanceId}", method = RequestMethod.PUT)
	@RolesAllowed(AuthoritiesConstants.ANONYMOUS)
	@ResponseBody
	Set<String> updateMetadata(@PathVariable String processInstanceId, @RequestBody ApplicationDTO application) {
		log.info("REST request to update metadata for process instance with id: {}, application {}", processInstanceId,
				application);
		Map<String, ApplicationDTO> params = new HashMap<String, ApplicationDTO>();
		params.put("application", application);
		// Return the updated value of the model
		return bpmn.unlockProcess(processInstanceId, BpmnServiceConstants.MSG_START_META_RETRIEVAL.toString(), params);
	}

}
