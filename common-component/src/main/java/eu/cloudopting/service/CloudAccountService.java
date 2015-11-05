package eu.cloudopting.service;

import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.dto.CloudAccountDTO;
import eu.cloudopting.events.api.service.BaseService;

public interface CloudAccountService extends BaseService<CloudAccounts> {

	CloudAccounts create(Long idOrganization, CloudAccountDTO cloudAccountDTO);
	
	public void update(CloudAccountDTO cloudAccountDTO);
}
