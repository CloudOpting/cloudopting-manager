package eu.cloudopting.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.dto.CloudAccountDTO;
import eu.cloudopting.events.api.service.BaseService;

public interface CloudAccountService extends BaseService<CloudAccounts> {

	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #idOrganization)")
	CloudAccounts create(Long idOrganization, CloudAccountDTO cloudAccountDTO);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #idOrganization)")
	void update(Long idOrganization, CloudAccountDTO cloudAccountDTO);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #idOrganization)")
	void delete(Long idOrganization, Long idCloudAccount);
	
	CloudAccounts find(Long idOrganization, Long idCloudAccount);
	
	List<CloudAccounts> findByOrganizationId(Long idOrganization);
}
