package eu.cloudopting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.cloudopting.domain.CloudAccounts;
import eu.cloudopting.events.api.repository.GenericRepository;

public interface CloudAccountRepository extends GenericRepository<CloudAccounts, Long> {

	@Query("SELECT ca FROM CloudAccounts ca where ca.organizationId.id = :organizationId and " +
			"(1 = ?#{hasRole('ROLE_ADMIN') ? 1 : 0} or " +
			"ca.organizationId.id = ?#{principal.organizationId} or " +
			"ca.isTrial = true)")
	List<CloudAccounts> findByOrganizationId(@Param("organizationId") Long organizationId);
}
