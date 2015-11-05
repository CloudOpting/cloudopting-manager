package eu.cloudopting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.events.api.repository.GenericRepository;

public interface OrganizationRepository extends GenericRepository<Organizations, Long> {

	@Query("SELECT o FROM Organizations o JOIN FETCH o.cloudAccountss i WHERE o.id = :organizationId")
	Organizations findOneAndInitCloudAccountCollection(@Param("organizationId") Long organizationId);
}
