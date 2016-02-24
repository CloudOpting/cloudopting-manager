package eu.cloudopting.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.dto.CloudAccountDTO;
import eu.cloudopting.dto.OrganizationDTO;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.CloudAccountService;
import eu.cloudopting.service.OrganizationService;

@RestController
@RequestMapping("/api")
public class OrganizationResource extends AbstractController<Organizations> {

	@Inject
	private OrganizationService organizationService;
	
	@Inject
	private CloudAccountService cloudAccountService;

	public OrganizationResource() {
		super(Organizations.class);
	}

	@RequestMapping(value = "/organization/{idOrganization}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<Organizations> findOne(@PathVariable("idOrganization") final Long idOrganization) {
		Organizations organization = ((OrganizationService)getService()).findOneAndInitCloudAccountCollection(idOrganization);
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
	public final ResponseEntity<Organizations> create(@Valid @RequestBody OrganizationDTO organizationDTO) {
		Organizations organization = ((OrganizationService)getService()).create(organizationDTO);
		return new ResponseEntity<>(organization, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/organization", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public final void update(@Valid @RequestBody OrganizationDTO organizationDTO, HttpServletResponse response) {
		((OrganizationService)getService()).update(organizationDTO);
	}

	@RequestMapping(value = "/organization/{idOrganization}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public final void delete(@PathVariable Long idOrganization, HttpServletResponse response){
		getService().delete(idOrganization);
	}

	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<CloudAccounts>> findAllCloudAccounts(@PathVariable Long idOrganization) {
		List<CloudAccounts> cloudAccounts = ((CloudAccountService)getCloudAccountService()).findByOrganizationId(idOrganization);
		return new ResponseEntity<>(cloudAccounts, HttpStatus.OK);
	}

	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts/{idCloudAccount}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<CloudAccounts> findCloudAccount(@PathVariable Long idOrganization, @PathVariable Long idCloudAccount) {
		CloudAccounts cloudAccount = ((CloudAccountService)getCloudAccountService()).find(idOrganization, idCloudAccount);
		return new ResponseEntity<>(cloudAccount, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts/{idCloudAccount}", method = RequestMethod.DELETE,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public final ResponseEntity<String> deleteCloudAccount(@PathVariable Long idOrganization, @PathVariable Long idCloudAccount,
			HttpServletResponse response) {
		((CloudAccountService)getCloudAccountService()).delete(idOrganization, idCloudAccount);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public final ResponseEntity<CloudAccounts> createCloudAccount(@Valid @RequestBody CloudAccountDTO cloudAccountDTO,
			@PathVariable Long idOrganization) {
		CloudAccounts cloudAccount = ((CloudAccountService)getCloudAccountService()).create(idOrganization, cloudAccountDTO);
		return new ResponseEntity<>(cloudAccount, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts", method = RequestMethod.PUT,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public final ResponseEntity<String> updateCloudAccount(@Valid @RequestBody CloudAccountDTO cloudAccountDTO,
			@PathVariable Long idOrganization) {
		((CloudAccountService)getCloudAccountService()).update(idOrganization, cloudAccountDTO);
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@Override
	protected BaseService<Organizations> getService() {
		return organizationService;
	}
	
	protected BaseService<CloudAccounts> getCloudAccountService() {
		return cloudAccountService;
	}
}
