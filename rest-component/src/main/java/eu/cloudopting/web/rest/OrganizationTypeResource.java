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

import eu.cloudopting.domain.OrganizationTypes;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.OrganizationTypeService;

@RestController
@RequestMapping("/api")
public class OrganizationTypeResource extends AbstractController<OrganizationTypes> {

	@Inject
	private OrganizationTypeService organizationTypeService;
	
	public OrganizationTypeResource() {
		super(OrganizationTypes.class);
	}

	@RequestMapping(value = "/organizationType", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<OrganizationTypes>> findAll() {
		List<OrganizationTypes> organizationTypes = getService().findAll();
		return new ResponseEntity<>(organizationTypes, HttpStatus.OK);
	}
	
	@Override
	protected BaseService<OrganizationTypes> getService() {
		return organizationTypeService;
	}
}
