package eu.cloudopting.service.impl;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.ApplicationMedia;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.ApplicationMediaRepository;
import eu.cloudopting.service.ApplicationMediaService;

@Service
@Transactional
public class ApplicationMediaServiceImpl extends AbstractService<ApplicationMedia> implements ApplicationMediaService {

    @Inject
    private ApplicationMediaRepository applicationMediaRepository;
    
    public ApplicationMediaServiceImpl() {
        super(ApplicationMedia.class);
    }

	@Override
	protected PagingAndSortingRepository<ApplicationMedia, Long> getDao() {
		return applicationMediaRepository;
	}

	@Override
	protected JpaSpecificationExecutor<ApplicationMedia> getSpecificationExecutor() {
		return applicationMediaRepository;
	}
}
