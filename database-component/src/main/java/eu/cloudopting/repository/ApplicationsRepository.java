package eu.cloudopting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.events.api.repository.GenericRepository;

/**
 * @author Daniel P.
 */
public interface ApplicationsRepository extends GenericRepository<Applications, Long> {
	
	@EntityGraph(value = "graph.api.application.GET", type = EntityGraphType.LOAD)
	@Query("SELECT a FROM Applications a")
	Page<Applications> findForApiGetAll(Pageable pageable);
	
	@EntityGraph(value = "graph.api.application.GET", type = EntityGraphType.LOAD)
	Applications findById(Long id);
}
