package eu.cloudopting.service;

import java.util.List;

import eu.cloudopting.domain.MonitoringInfoElastic;
import eu.cloudopting.dto.MonitoringInfoElasticDTO;
import eu.cloudopting.events.api.service.BaseService;

public interface MonitoringInfoElasticService extends BaseService<MonitoringInfoElastic> {

	List<MonitoringInfoElastic> findByCustomizationId(Long customizationId);

	MonitoringInfoElastic create(MonitoringInfoElasticDTO monitoringInfoElasticDTO);

	void update(MonitoringInfoElasticDTO monitoringInfoElasticDTO);
}
