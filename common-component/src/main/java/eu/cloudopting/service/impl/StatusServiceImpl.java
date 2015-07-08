package eu.cloudopting.service.impl;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Status;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.StatusRepository;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.StatusService;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Created by danielpo on 08/07/2015.
 */
@Service
@Transactional
public class StatusServiceImpl extends AbstractService<Status> implements StatusService {

    @Inject
    StatusRepository statusRepository;
    /**
     * Constructorul service-ului.
     *
     */
    public StatusServiceImpl() {
        super(Status.class);
    }

    @Override
    protected PagingAndSortingRepository<Status, Long> getDao() {
        return statusRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Status> getSpecificationExecutor() {
        return statusRepository;
    }
}
