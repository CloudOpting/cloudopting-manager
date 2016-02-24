package eu.cloudopting.service;

import org.springframework.security.access.prepost.PreAuthorize;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.dto.OrganizationDTO;
import eu.cloudopting.events.api.service.BaseService;

public interface OrganizationService extends BaseService<Organizations> {

	@PreAuthorize("hasRole('ROLE_ADMIN') or (#organizationDTO.organizationStatus.id == T(eu.cloudopting.domain.util.OrganizationStatusEnum).Pending.id)")
	Organizations create(OrganizationDTO organizationDTO);

	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #organizationDTO.id)")
	void update(OrganizationDTO organizationDTO);

	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #organizationId)")
	Organizations findOneAndInitCloudAccountCollection(Long organizationId);
}
