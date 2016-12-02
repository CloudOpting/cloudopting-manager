package eu.cloudopting.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.ApplicationsRepository;
import eu.cloudopting.service.ApplicationService;

/**
 * @author Daniel P.
 */
@Service
@Transactional
public class ApplicationServiceImpl extends AbstractService<Applications> implements ApplicationService {

    private final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

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
        if(filterObj != null){
        	final Specification<Applications> filterSpec = createFilterSpecifications(filterObj);
        	return applicationsRepository.findAll(filterSpec, new PageRequest(page, size, sortInfo));
        }
        
        return applicationsRepository.findAll(new PageRequest(page, size, sortInfo));
	}

	@Override
	@Transactional(readOnly = true)
	public Applications findForApiGetOne(Long id) {
		return applicationsRepository.findById(id);
	}
}
