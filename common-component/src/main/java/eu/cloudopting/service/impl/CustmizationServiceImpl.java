package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.domain.CustomizationStatus;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.dto.CustomizationDTO;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.CustomizationRepository;
import eu.cloudopting.service.CloudAccountService;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.service.CustomizationStatusService;

/**
 * Created by danielpo on 19/03/2015.
 */
@Service
@Transactional
public class CustmizationServiceImpl extends AbstractService<Customizations> implements CustomizationService {

    @Inject
    CustomizationRepository customizationRepository;

    @Inject
    CloudAccountService cloudAccountService;
    
    @Inject
    CustomizationStatusService customizationStatusService;
    
    /**
     * Constructorul service-ului.
     */
    public CustmizationServiceImpl() {
        super(Customizations.class);
    }

    @Override
	public void update(CustomizationDTO customizationDTO) {
    	Customizations customization = findOne(customizationDTO.getId());
    	copyPropertiesFromDto(customizationDTO, customization);
    	if(customizationDTO.getCloudAccountId() != null){
    		setCloudAccount(customization, customizationDTO.getCloudAccountId());
    	}
    	if(customizationDTO.getStatusId() != null){
    		setStatus(customization, customizationDTO.getStatusId());
    	}
    	update(customization);
    }
    
    private void copyPropertiesFromDto(CustomizationDTO source, Customizations target){
		if(source.getPayService() != null){
			target.setPayService(source.getPayService());
		}
		if(source.getPayPlatform() != null){
			target.setPayPlatform(source.getPayPlatform());
		}
		if(source.getIsTrial() != null){
			target.setIsTrial(source.getIsTrial());
		}
		if(source.getTrialEndDate() != null){
			target.setTrialEndDate(source.getTrialEndDate());
		}
	}
    
	private void setCloudAccount(Customizations customization, Long cloudAccountId){
		CloudAccounts cloudAccount = cloudAccountService.findOne(cloudAccountId);
		if(cloudAccount == null){
			throw new IllegalArgumentException("Cannot find cloud account with id: " + cloudAccountId);
		}
		customization.setCloudAccount(cloudAccount);
	}
	
	private void setStatus(Customizations customization, Long statusId) {
		CustomizationStatus status = customizationStatusService.findOne(statusId);
		if(status == null){
			throw new IllegalArgumentException("Cannot find customization status with id: " + statusId);
		}
		customization.setStatusId(status);
	}
	
    @Override
    protected PagingAndSortingRepository<Customizations, Long> getDao() {
        return customizationRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Customizations> getSpecificationExecutor() {
        return customizationRepository;
    }
}
