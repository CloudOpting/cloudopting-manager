package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.CustomizationDeployInfo;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.CustomizationDeployInfoRepository;
import eu.cloudopting.service.CustomizationDeployInfoService;

@Service
@Transactional
public class CustomizationDeployInfoServiceImpl extends AbstractService<CustomizationDeployInfo> 
		implements CustomizationDeployInfoService {

	@Inject
	private CustomizationDeployInfoRepository customizationDeployInfoRepository;
	
	public CustomizationDeployInfoServiceImpl() {
		super(CustomizationDeployInfo.class);
	}

	@Override
	protected PagingAndSortingRepository<CustomizationDeployInfo, Long> getDao() {
		return customizationDeployInfoRepository;
	}

	@Override
	protected JpaSpecificationExecutor<CustomizationDeployInfo> getSpecificationExecutor() {
		return customizationDeployInfoRepository;
	}
}
