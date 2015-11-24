package eu.cloudopting.service;

import eu.cloudopting.domain.Contact;
import eu.cloudopting.dto.ContactDTO;
import eu.cloudopting.events.api.service.BaseService;

public interface ContactService extends BaseService<Contact> {

	Contact create(ContactDTO contactDTO);
}
