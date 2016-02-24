package eu.cloudopting.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import eu.cloudopting.domain.MonitoringInfoElastic;
import eu.cloudopting.dto.MonitoringInfoElasticDTO;
import eu.cloudopting.events.api.service.BaseService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface MonitoringInfoElasticService extends BaseService<MonitoringInfoElastic> {

	List<MonitoringInfoElastic> findByCustomizationId(Long customizationId);

	MonitoringInfoElastic create(MonitoringInfoElasticDTO monitoringInfoElasticDTO);

	void update(MonitoringInfoElasticDTO monitoringInfoElasticDTO);
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<MonitoringInfoElastic> findAll();
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	MonitoringInfoElastic findOne(long id);
}
