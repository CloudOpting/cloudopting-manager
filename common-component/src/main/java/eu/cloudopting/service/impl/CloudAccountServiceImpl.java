package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.Providers;
import eu.cloudopting.dto.CloudAccountDTO;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.CloudAccountRepository;
import eu.cloudopting.service.CloudAccountService;
import eu.cloudopting.service.OrganizationService;
import eu.cloudopting.service.ProviderService;

@Service
@Transactional
public class CloudAccountServiceImpl extends AbstractService<CloudAccounts> implements CloudAccountService {

	@Inject
	private CloudAccountRepository cloudAccountRepository;
	
	@Inject
	private OrganizationService organizationService;
	
	@Inject
	private ProviderService providerService;
	
	public CloudAccountServiceImpl() {
		super(CloudAccounts.class);
	}

	@Override
	public CloudAccounts create(Long idOrganization, CloudAccountDTO cloudAccountDTO) {
		CloudAccounts cloudAccount = new CloudAccounts();
		Organizations organization = organizationService.findOne(idOrganization);
		Providers provider = providerService.findOne(cloudAccountDTO.getProvider().getId());
		
		BeanUtils.copyProperties(cloudAccountDTO, cloudAccount);
		cloudAccount.setOrganizationId(organization);
		organization.getCloudAccountss().add(cloudAccount);
		
		cloudAccount.setProviderId(provider);
		provider.getCloudAccountss().add(cloudAccount);
		return create(cloudAccount);
	}
	
	@Override
	public void update(CloudAccountDTO cloudAccountDTO) {
		CloudAccounts cloudAccount = findOne(cloudAccountDTO.getId());
		Providers provider = providerService.findOne(cloudAccountDTO.getProvider().getId());
		
		BeanUtils.copyProperties(cloudAccountDTO, cloudAccount);
		cloudAccount.setProviderId(provider);
		provider.getCloudAccountss().add(cloudAccount);
		update(cloudAccount);
	}
	
	@Override
	protected PagingAndSortingRepository<CloudAccounts, Long> getDao() {
		return cloudAccountRepository;
	}

	@Override
	protected JpaSpecificationExecutor<CloudAccounts> getSpecificationExecutor() {
		return cloudAccountRepository;
	}
}
