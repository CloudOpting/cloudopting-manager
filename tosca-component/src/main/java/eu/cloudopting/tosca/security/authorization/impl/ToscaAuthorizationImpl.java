package eu.cloudopting.tosca.security.authorization.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.SecurityUser;
import eu.cloudopting.events.api.preconditions.ServicePreconditions;
import eu.cloudopting.repository.ApplicationsRepository;
import eu.cloudopting.security.SecurityUtils;
import eu.cloudopting.tosca.security.authorization.ToscaAuthorization;

@Service(value="toscaAuthorization")
@Transactional(readOnly = true)
public class ToscaAuthorizationImpl implements ToscaAuthorization{

	@Inject
    private ApplicationsRepository applicationsRepository;
	
	@Override
	public boolean hasReadCustomizationPermission(String idApp){
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
	public boolean hasWriteCustomizationPermission(String idApp){
		Applications application = applicationsRepository.findOne(Long.valueOf(idApp));
		ServicePreconditions.checkEntityExists(application);
		SecurityUser currentUser = SecurityUtils.getCurrentLoggedInUser();
		if(application.getOrganizationId() == null ||
				application.getOrganizationId().getId().compareTo(currentUser.getOrganizationId()) != 0){
			return false;
		}
		return true;
	}
}
