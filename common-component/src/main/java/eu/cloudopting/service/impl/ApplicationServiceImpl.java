package eu.cloudopting.service.impl;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.repository.ApplicationsRepository;
import eu.cloudopting.service.ApplicationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.events.api.service.AbstractService;

import javax.inject.Inject;

/**
 * @author Daniel P.
 */
@Service
@Transactional
public class ApplicationServiceImpl extends AbstractService<Applications> implements ApplicationService {

    @Inject
    private ApplicationsRepository applicationsRepository;

    /**
     * Constructorul service-ului.
     */
    public ApplicationServiceImpl() {
        super(Applications.class);
    }

    @Override
    protected PagingAndSortingRepository<Applications, Long> getDao() {
        return applicationsRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Applications> getSpecificationExecutor() {
        return applicationsRepository;
    }

	@Override
	@Transactional(readOnly = true)
	public Page<Applications> findForApiGetAll(int page, int size, String sortBy, String sortOrder,
			String filterObj) {
        final Sort sortInfo = constructSort(sortBy, sortOrder);
        return applicationsRepository.findForApiGetAll(new PageRequest(page, size, sortInfo));
	}

	@Override
	@Transactional(readOnly = true)
	public Applications findForApiGetOne(Long id) {
		return applicationsRepository.findById(id);
	}
}
