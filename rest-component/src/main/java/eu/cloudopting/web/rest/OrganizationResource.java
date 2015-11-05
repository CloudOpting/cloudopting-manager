package eu.cloudopting.web.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<Set<CloudAccounts>> findAllCloudAccounts(@PathVariable Long idOrganization) {
		Organizations organization = ((OrganizationService)getService()).findOneAndInitCloudAccountCollection(idOrganization);
		if (organization == null) {
			return new ResponseEntity<>(new HashSet<>(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(organization.getCloudAccountss(), HttpStatus.OK);
	}

	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts/{idCloudAccount}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<CloudAccounts> findCloudAccount(@PathVariable Long idOrganization, @PathVariable Long idCloudAccount) {
		Organizations organization = ((OrganizationService)getService()).findOneAndInitCloudAccountCollection(idOrganization);
		CloudAccounts cloudAccount = getCloudAccountService().findOne(idCloudAccount);
		if (organization == null || cloudAccount == null ||
				!cloudAccount.getOrganizationId().getId().equals(organization.getId())){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(cloudAccount, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts/{idCloudAccount}", method = RequestMethod.DELETE,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public final ResponseEntity<String> deleteCloudAccount(@PathVariable Long idOrganization, @PathVariable Long idCloudAccount,
			HttpServletResponse response) {
		Organizations organization = ((OrganizationService)getService()).findOneAndInitCloudAccountCollection(idOrganization);
		if (organization == null) {
			return new ResponseEntity<>("Organization with id " + idOrganization + " not found", HttpStatus.NOT_FOUND);
		}
		CloudAccounts cloudAccount = getCloudAccountService().findOne(idCloudAccount);
		if (cloudAccount == null) {
			return new ResponseEntity<>("CloudAccount with id " + idCloudAccount + " not found", HttpStatus.NOT_FOUND);
		}
		if (!organization.getId().equals(cloudAccount.getOrganizationId().getId())) {
			return new ResponseEntity<>("CloudAccount with id " + idCloudAccount + " is not associated with organization with id " + idOrganization, 
					HttpStatus.NOT_FOUND);
		}
		getCloudAccountService().delete(idCloudAccount);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public final ResponseEntity<CloudAccounts> createCloudAccount(@Valid @RequestBody CloudAccountDTO cloudAccountDTO,
			@PathVariable Long idOrganization) {
		Organizations organization = getService().findOne(idOrganization);
		if(organization == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		CloudAccounts cloudAccount = ((CloudAccountService)getCloudAccountService()).create(idOrganization, cloudAccountDTO);
		return new ResponseEntity<>(cloudAccount, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/organization/{idOrganization}/cloudaccounts", method = RequestMethod.PUT,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public final ResponseEntity<String> updateCloudAccount(@Valid @RequestBody CloudAccountDTO cloudAccountDTO,
			@PathVariable Long idOrganization) {
		Organizations organization = ((OrganizationService)getService()).findOneAndInitCloudAccountCollection(idOrganization);
		if(organization == null) {
			return new ResponseEntity<>("Organization with id " + idOrganization + " not found", HttpStatus.NOT_FOUND);
		}
		CloudAccounts cloudAccount = getCloudAccountService().findOne(cloudAccountDTO.getId());
		if (cloudAccount == null) {
			return new ResponseEntity<>("CloudAccount with id " + cloudAccountDTO.getId() + " not found", HttpStatus.NOT_FOUND);
		}
		if (!cloudAccount.getOrganizationId().getId().equals(organization.getId())) {
			return new ResponseEntity<>("CloudAccount with id " + cloudAccountDTO.getId() + " is not associated with organization with id " + 
					idOrganization, HttpStatus.NOT_FOUND);
		}
		((CloudAccountService)getCloudAccountService()).update(cloudAccountDTO);
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
