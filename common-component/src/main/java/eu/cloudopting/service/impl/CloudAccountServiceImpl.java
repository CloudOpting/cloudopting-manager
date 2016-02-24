package eu.cloudopting.service.impl;

import java.util.List;

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
import eu.cloudopting.events.api.exceptions.ResourceNotFoundException;
import eu.cloudopting.events.api.preconditions.ServicePreconditions;
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
		Organizations organization = organizationService.findOne(idOrganization);
		ServicePreconditions.checkEntityExists(organization);
		
		CloudAccounts cloudAccount = new CloudAccounts();
		Providers provider = providerService.findOne(cloudAccountDTO.getProvider().getId());
		
		BeanUtils.copyProperties(cloudAccountDTO, cloudAccount);
		cloudAccount.setOrganizationId(organization);
		organization.getCloudAccountss().add(cloudAccount);
		
		cloudAccount.setProviderId(provider);
		provider.getCloudAccountss().add(cloudAccount);
		return create(cloudAccount);
	}

	@Override
	public void update(Long idOrganization, CloudAccountDTO cloudAccountDTO) {
		Organizations organization = organizationService.findOne(idOrganization);
		ServicePreconditions.checkEntityExists(organization);
		CloudAccounts cloudAccount = findOne(cloudAccountDTO.getId());
		ServicePreconditions.checkEntityExists(cloudAccount);
		if (!cloudAccount.getOrganizationId().getId().equals(organization.getId())) {
			throw new ResourceNotFoundException("Not found");
		}
		
		Providers provider = providerService.findOne(cloudAccountDTO.getProvider().getId());
		
		BeanUtils.copyProperties(cloudAccountDTO, cloudAccount);
		cloudAccount.setProviderId(provider);
		provider.getCloudAccountss().add(cloudAccount);
		update(cloudAccount);
	}
	
	@Override
	public void delete(Long idOrganization, Long idCloudAccount) {
		Organizations organization = organizationService.findOne(idOrganization);
		ServicePreconditions.checkEntityExists(organization);
		CloudAccounts cloudAccount = findOne(idCloudAccount);
		ServicePreconditions.checkEntityExists(cloudAccount);
		if (!organization.getId().equals(cloudAccount.getOrganizationId().getId())) {
			throw new ResourceNotFoundException("Not found");
		}
		delete(idCloudAccount);
	}
	
	@Override
	public CloudAccounts find(Long idOrganization, Long idCloudAccount) {
		Organizations organization = organizationService.findOne(idOrganization);
		ServicePreconditions.checkEntityExists(organization);
		CloudAccounts cloudAccount = findOne(idCloudAccount);
		ServicePreconditions.checkEntityExists(cloudAccount);
		if (!organization.getId().equals(cloudAccount.getOrganizationId().getId())){
			throw new ResourceNotFoundException("Not found");
		}
		return cloudAccount;
	}
	
	@Override
	public List<CloudAccounts> findByOrganizationId(Long idOrganization) {
		Organizations organization = organizationService.findOne(idOrganization);
		ServicePreconditions.checkEntityExists(organization);
		return cloudAccountRepository.findByOrganizationId(idOrganization);
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
