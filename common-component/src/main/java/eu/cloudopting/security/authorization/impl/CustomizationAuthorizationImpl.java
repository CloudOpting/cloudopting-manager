package eu.cloudopting.security.authorization.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.SecurityUser;
import eu.cloudopting.events.api.preconditions.ServicePreconditions;
import eu.cloudopting.repository.CustomizationRepository;
import eu.cloudopting.security.SecurityUtils;
import eu.cloudopting.security.authorization.CustomizationAuthorization;

@Service(value="customizationAuthorization")
@Transactional(readOnly = true)
public class CustomizationAuthorizationImpl implements CustomizationAuthorization{

	@Inject
    private CustomizationRepository customizationRepository;
	
	@Override
	public boolean hasWriteCustomizationPermission(Long customizationId){
		Customizations customization = customizationRepository.findOne(customizationId);
		ServicePreconditions.checkEntityExists(customization);
		SecurityUser currentUser = SecurityUtils.getCurrentLoggedInUser();
		if(customization.getCustomerOrganizationId() == null ||
				customization.getCustomerOrganizationId().getId().compareTo(currentUser.getOrganizationId()) != 0){
			return false;
		}
		return true;
	}
}
