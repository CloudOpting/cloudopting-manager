package eu.cloudopting.service;

import org.springframework.data.domain.Page;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.events.api.service.BaseService;

/**
 * @author Daniel P.
 */

public interface ApplicationService extends BaseService<Applications> {
	
	Page<Applications> findForApiGetAll(int page, int size, String sortBy, String sortOrder,
			String filterObj);
}
