package eu.cloudopting.service;

import org.springframework.security.access.prepost.PreAuthorize;

import eu.cloudopting.domain.ApplicationMedia;
import eu.cloudopting.events.api.service.BaseService;

public interface ApplicationMediaService extends BaseService<ApplicationMedia> {

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #entity.applicationId.organizationId.id)")
	ApplicationMedia create(ApplicationMedia entity);
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #entity.applicationId.organizationId.id)")
	void update(ApplicationMedia entity);
}
