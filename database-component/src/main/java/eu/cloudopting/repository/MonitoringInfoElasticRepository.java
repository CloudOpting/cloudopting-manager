package eu.cloudopting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.cloudopting.domain.MonitoringInfoElastic;
import eu.cloudopting.events.api.repository.GenericRepository;

public interface MonitoringInfoElasticRepository extends GenericRepository<MonitoringInfoElastic, Long> {

	@Query("SELECT info FROM MonitoringInfoElastic info where info.customization.id = :customizationId")
	List<MonitoringInfoElastic> findByCustomizationId(@Param("customizationId") Long customizationId);
}
