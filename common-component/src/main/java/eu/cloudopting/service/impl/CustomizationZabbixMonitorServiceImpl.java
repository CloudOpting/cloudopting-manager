package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.CustomizationZabbixMonitor;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.CustomizationZabbixMonitorRepository;
import eu.cloudopting.service.CustomizationZabbixMonitorService;

@Service
@Transactional
public class CustomizationZabbixMonitorServiceImpl extends AbstractService<CustomizationZabbixMonitor> 
		implements CustomizationZabbixMonitorService {

	@Inject
	private CustomizationZabbixMonitorRepository customizationZabbixMonitorRepository;
	
	public CustomizationZabbixMonitorServiceImpl() {
		super(CustomizationZabbixMonitor.class);
	}

	@Override
	protected PagingAndSortingRepository<CustomizationZabbixMonitor, Long> getDao() {
		return customizationZabbixMonitorRepository;
	}

	@Override
	protected JpaSpecificationExecutor<CustomizationZabbixMonitor> getSpecificationExecutor() {
		return customizationZabbixMonitorRepository;
	}
}
