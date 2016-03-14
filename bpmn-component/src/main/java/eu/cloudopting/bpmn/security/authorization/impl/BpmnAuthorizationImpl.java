package eu.cloudopting.bpmn.security.authorization.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.bpmn.security.authorization.BpmnAuthorization;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.SecurityUser;
import eu.cloudopting.events.api.preconditions.ServicePreconditions;
import eu.cloudopting.repository.ApplicationsRepository;
import eu.cloudopting.repository.CustomizationRepository;
import eu.cloudopting.security.SecurityUtils;

@Service(value="bpmnAuthorization")
@Transactional(readOnly = true)
public class BpmnAuthorizationImpl implements BpmnAuthorization{

	@Inject
    private ApplicationsRepository applicationsRepository;
	
	@Inject
    private CustomizationRepository customizationRepository;
	
	@Override
	public boolean hasWriteApplicationPermission(String idApp){
		Applications application = applicationsRepository.findOne(Long.valueOf(idApp));
		ServicePreconditions.checkEntityExists(application);
		SecurityUser currentUser = SecurityUtils.getCurrentLoggedInUser();
		if(application.getOrganizationId() == null ||
				application.getOrganizationId().getId().compareTo(currentUser.getOrganizationId()) != 0){
			return false;
		}
		return true;
	}

	@Override
	public boolean hasWriteCustomizationPermission(String idCustomization) {
		Customizations customization = customizationRepository.findOne(Long.valueOf(idCustomization));
		ServicePreconditions.checkEntityExists(customization);
		SecurityUser currentUser = SecurityUtils.getCurrentLoggedInUser();
		if(customization.getCustomerOrganizationId() == null ||
				customization.getCustomerOrganizationId().getId().compareTo(currentUser.getOrganizationId()) != 0){
			return false;
		}
		return true;
	}
}
