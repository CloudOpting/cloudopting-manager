package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.OrganizationStatus;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.OrganizationStatusRepository;
import eu.cloudopting.service.OrganizationStatusService;

@Service
@Transactional
public class OrganizationStatusServiceImpl extends AbstractService<OrganizationStatus> implements OrganizationStatusService {

	@Inject
	private OrganizationStatusRepository organizationStatusRepository;
	
	public OrganizationStatusServiceImpl() {
		super(OrganizationStatus.class);
	}
	
	@Override
	protected PagingAndSortingRepository<OrganizationStatus, Long> getDao() {
		return organizationStatusRepository;
	}

	@Override
	protected JpaSpecificationExecutor<OrganizationStatus> getSpecificationExecutor() {
		return organizationStatusRepository;
	}
}
