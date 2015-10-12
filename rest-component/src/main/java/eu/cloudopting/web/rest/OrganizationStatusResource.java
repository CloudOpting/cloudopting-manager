package eu.cloudopting.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.OrganizationStatus;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.OrganizationStatusService;

@RestController
@RequestMapping("/api")
public class OrganizationStatusResource extends AbstractController<OrganizationStatus> {

	@Inject
	private OrganizationStatusService organizationStatusService;
	
	public OrganizationStatusResource() {
		super(OrganizationStatus.class);
	}
	
	@RequestMapping(value = "/organizationStatus", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<OrganizationStatus>> findAll() {
		List<OrganizationStatus> organizationStatusList = getService().findAll();
		return new ResponseEntity<>(organizationStatusList, HttpStatus.OK);
	}
	
	@Override
	protected BaseService<OrganizationStatus> getService() {
		return organizationStatusService;
	}
}
