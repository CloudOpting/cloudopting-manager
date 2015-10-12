package eu.cloudopting.service;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.dto.OrganizationDTO;
import eu.cloudopting.events.api.service.BaseService;

public interface OrganizationService extends BaseService<Organizations> {

	Organizations create(OrganizationDTO organizationDTO);

	void update(OrganizationDTO organizationDTO);
}
