package eu.cloudopting.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.MonitoringInfoElastic;
import eu.cloudopting.dto.MonitoringInfoElasticDTO;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.MonitoringInfoElasticRepository;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.service.MonitoringInfoElasticService;

@Service
@Transactional
public class MonitoringInfoElasticServiceImpl extends AbstractService<MonitoringInfoElastic> implements MonitoringInfoElasticService {

	@Inject
	private MonitoringInfoElasticRepository monitoringInfoElasticRepository;
	
	@Inject
	private CustomizationService customizationService;
	
	public MonitoringInfoElasticServiceImpl() {
		super(MonitoringInfoElastic.class);
	}
	
	@Override
	public List<MonitoringInfoElastic> findByCustomizationId(Long customizationId) {
		return monitoringInfoElasticRepository.findByCustomizationId(customizationId);
	}
	
	@Override
	public MonitoringInfoElastic create(MonitoringInfoElasticDTO monitoringInfoElasticDTO) {
		MonitoringInfoElastic monitoringInfoElastic = new MonitoringInfoElastic();
		BeanUtils.copyProperties(monitoringInfoElasticDTO, monitoringInfoElastic);
		setCustomization(monitoringInfoElastic, monitoringInfoElasticDTO.getCustomizationId());
		return create(monitoringInfoElastic);
	}
	
	@Override
	public void update(MonitoringInfoElasticDTO monitoringInfoElasticDTO) {
		MonitoringInfoElastic monitoringInfoElastic = findOne(monitoringInfoElasticDTO.getId());
		BeanUtils.copyProperties(monitoringInfoElasticDTO, monitoringInfoElastic);
		setCustomization(monitoringInfoElastic, monitoringInfoElasticDTO.getCustomizationId());
		update(monitoringInfoElastic);
	}
	
	private void setCustomization(MonitoringInfoElastic monitoringInfoElastic, Long customizationId){
		Customizations customization = customizationService.findOne(customizationId);
		if(customization == null){
			throw new IllegalArgumentException("Cannot find customization with id: " + customizationId);
		}
		monitoringInfoElastic.setCustomization(customization);
	}
	
	@Override
	protected PagingAndSortingRepository<MonitoringInfoElastic, Long> getDao() {
		return monitoringInfoElasticRepository;
	}

	@Override
	protected JpaSpecificationExecutor<MonitoringInfoElastic> getSpecificationExecutor() {
		return monitoringInfoElasticRepository;
	}
}
