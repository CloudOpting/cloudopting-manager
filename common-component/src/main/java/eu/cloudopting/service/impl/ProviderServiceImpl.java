package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Providers;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.ProviderRepository;
import eu.cloudopting.service.ProviderService;

@Service
@Transactional
public class ProviderServiceImpl extends AbstractService<Providers> implements ProviderService {

	@Inject
	private ProviderRepository providerRepository;
	
	public ProviderServiceImpl() {
		super(Providers.class);
	}

	@Override
	protected PagingAndSortingRepository<Providers, Long> getDao() {
		return providerRepository;
	}

	@Override
	protected JpaSpecificationExecutor<Providers> getSpecificationExecutor() {
		return providerRepository;
	}
}
