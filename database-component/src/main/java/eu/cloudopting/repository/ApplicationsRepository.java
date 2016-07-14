package eu.cloudopting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.events.api.repository.GenericRepository;

/**
 * @author Daniel P.
 */
public interface ApplicationsRepository extends GenericRepository<Applications, Long> {
	
	@EntityGraph(value = "graph.api.application.GET", type = EntityGraphType.LOAD)
	Page<Applications> findAll(Specification<Applications> spec, Pageable pageable);
	
	@EntityGraph(value = "graph.api.application.GET", type = EntityGraphType.LOAD)
	Page<Applications> findAll(Pageable pageable);
	
	@EntityGraph(value = "graph.api.application.GET", type = EntityGraphType.LOAD)
	Applications findById(Long id);
}
