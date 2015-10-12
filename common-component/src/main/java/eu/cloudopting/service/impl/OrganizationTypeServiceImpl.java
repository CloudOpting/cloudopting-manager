package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.OrganizationTypes;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.OrganizationTypeRepository;
import eu.cloudopting.service.OrganizationTypeService;

@Service
@Transactional
public class OrganizationTypeServiceImpl extends AbstractService<OrganizationTypes> implements OrganizationTypeService {

	@Inject
	private OrganizationTypeRepository organizationTypeRepository;

	public OrganizationTypeServiceImpl() {
		super(OrganizationTypes.class);
	}
	
	@Override
	protected PagingAndSortingRepository<OrganizationTypes, Long> getDao() {
		return organizationTypeRepository;
	}

	@Override
	protected JpaSpecificationExecutor<OrganizationTypes> getSpecificationExecutor() {
		return organizationTypeRepository;
	}
}
