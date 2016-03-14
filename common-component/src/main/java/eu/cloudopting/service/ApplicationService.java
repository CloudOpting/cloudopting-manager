package eu.cloudopting.service;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.events.api.service.BaseService;

/**
 * @author Daniel P.
 */

public interface ApplicationService extends BaseService<Applications> {
	
	Page<Applications> findForApiGetAll(int page, int size, String sortBy, String sortOrder,
			String filterObj);
	
	Applications findForApiGetOne(Long id);
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #entity.organizationId.id)")
	Applications create(Applications entity);
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN') or (principal.organizationId == #entity.organizationId.id)")
	void update(Applications entity);
}
