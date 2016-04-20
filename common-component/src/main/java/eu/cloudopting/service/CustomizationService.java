package eu.cloudopting.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.dto.CustomizationDTO;
import eu.cloudopting.events.api.service.BaseService;

/**
 * Created by danielpo on 19/03/2015.
 */
public interface CustomizationService extends BaseService<Customizations> {

	@PreAuthorize("hasRole('ROLE_ADMIN') or @customizationAuthorization.hasWriteCustomizationPermission(#customizationDTO.id)")
	void update(CustomizationDTO customizationDTO);
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUBSCRIBER') or (principal.organizationId == #customization.customerOrganizationId.id)")
	Customizations create(Customizations customization);
	
	Customizations findOneByCurrentUserOrg(long customizationId);
	
	List<Customizations> findAllByCurrentUserOrg();
}
