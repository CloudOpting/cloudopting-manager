package eu.cloudopting.service;

import eu.cloudopting.domain.Applications;
import org.springframework.stereotype.Service;
import eu.cloudopting.events.api.service.BaseService;

import javax.transaction.Transactional;

/**
 * @author Daniel P.
 */

public interface ApplicationService extends BaseService<Applications> {
}
