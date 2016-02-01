package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.ApplicationSize;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.ApplicationSizeRepository;
import eu.cloudopting.service.ApplicationSizeService;

@Service
@Transactional
public class ApplicationSizeServiceImpl extends AbstractService<ApplicationSize> implements ApplicationSizeService {

	@Inject
	private ApplicationSizeRepository applicationSizeRepository;
	
	public ApplicationSizeServiceImpl() {
		super(ApplicationSize.class);
	}

	@Override
	protected PagingAndSortingRepository<ApplicationSize, Long> getDao() {
		return applicationSizeRepository;
	}

	@Override
	protected JpaSpecificationExecutor<ApplicationSize> getSpecificationExecutor() {
		return applicationSizeRepository;
	}
}
