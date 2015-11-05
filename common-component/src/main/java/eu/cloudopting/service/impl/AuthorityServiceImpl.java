package eu.cloudopting.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Authority;
import eu.cloudopting.repository.AuthorityRepository;
import eu.cloudopting.service.AuthorityService;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

	@Inject
	private AuthorityRepository authorityRepository;
	
	@Override
	public List<Authority> findAll() {
		return authorityRepository.findAll();
	}
}
