package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.CustomizationStatus;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.CustomizationStatusRepository;
import eu.cloudopting.service.CustomizationStatusService;

@Service
@Transactional
public class CustomizationStatusServiceImpl extends AbstractService<CustomizationStatus> implements CustomizationStatusService {

	@Inject
	private CustomizationStatusRepository customizationStatusRepository;
	
	public CustomizationStatusServiceImpl() {
		super(CustomizationStatus.class);
	}
	
	@Override
	protected PagingAndSortingRepository<CustomizationStatus, Long> getDao() {
		return customizationStatusRepository;
	}

	@Override
	protected JpaSpecificationExecutor<CustomizationStatus> getSpecificationExecutor() {
		return customizationStatusRepository;
	}
}
