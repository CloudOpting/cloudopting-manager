package eu.cloudopting.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.dto.OrganizationDTO;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.OrganizationService;

@RestController
@RequestMapping("/api")
public class OrganizationResource extends AbstractController<Organizations> {

	@Inject
	private OrganizationService organizationService;

	public OrganizationResource() {
		super(Organizations.class);
	}

	@RequestMapping(value = "/organization/{idOrganization}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<Organizations> findOne(@PathVariable("idOrganization") final Long idOrganization) {
		Organizations organization = getService().findOne(idOrganization);
		if(organization == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(organization, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/organization", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<Organizations>> findAll() {
		List<Organizations> organizations = getService().findAll();
		return new ResponseEntity<>(organizations, HttpStatus.OK);
	}

	@RequestMapping(value = "/organization", method = RequestMethod.POST)
	public final ResponseEntity<Organizations> create(@RequestBody OrganizationDTO organizationDTO) {
		Organizations organization = ((OrganizationService)getService()).create(organizationDTO);
		return new ResponseEntity<>(organization, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/organization", method = RequestMethod.PUT)
	public final void update(@RequestBody OrganizationDTO organizationDTO, HttpServletResponse response) {
		Organizations organization = getService().findOne(organizationDTO.getId());
		if (organization == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		} 
		response.setStatus(HttpServletResponse.SC_OK);
		((OrganizationService)getService()).update(organizationDTO);
	}

	@RequestMapping(value = "/organization/{idOrganization}", method = RequestMethod.DELETE)
	public final void delete(@PathVariable Long idOrganization, HttpServletResponse response){
		Organizations organization = getService().findOne(idOrganization);
		if (organization == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		getService().delete(idOrganization);
	}

	@Override
	protected BaseService<Organizations> getService() {
		return organizationService;
	}
}
