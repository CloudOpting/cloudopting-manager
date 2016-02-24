package eu.cloudopting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.events.api.repository.GenericRepository;

public interface OrganizationRepository extends GenericRepository<Organizations, Long> {

	@Query("SELECT o FROM Organizations o left JOIN FETCH o.cloudAccountss i WHERE o.id = :organizationId")
	Organizations findOneAndInitCloudAccountCollection(@Param("organizationId") Long organizationId);
	
	@Override
	@Query("SELECT o FROM Organizations o where " +
			"1 = ?#{hasRole('ROLE_ADMIN') ? 1 : 0} or " +
			"o.id = ?#{principal.organizationId}")
	List<Organizations> findAll();
}
