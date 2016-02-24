package eu.cloudopting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.events.api.repository.GenericRepository;

/**
 * Created by danielpo on 19/03/2015.
 */
public interface CustomizationRepository extends GenericRepository<Customizations, Long> {

	@Query("SELECT c FROM Customizations c where c.id = :customizationId and " +
			"(1 = ?#{hasRole('ROLE_ADMIN') ? 1 : 0} or " +
			"c.customerOrganizationId.id = ?#{principal.organizationId})")
	Customizations findOneByCurrentUserOrg(@Param("customizationId") long customizationId);

	@Query("SELECT c FROM Customizations c where " +
			"1 = ?#{hasRole('ROLE_ADMIN') ? 1 : 0} or " +
			"c.customerOrganizationId.id = ?#{principal.organizationId})")
	List<Customizations> findAllByCurrentUserOrg();
}
