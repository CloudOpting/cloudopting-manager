package eu.cloudopting.web.rest;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.Contact;
import eu.cloudopting.dto.ContactDTO;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.ContactService;

@RestController
@RequestMapping("/api")
public class ContactResource extends AbstractController<Contact> {

	@Inject
	private ContactService contactService;
	
	public ContactResource() {
		super(Contact.class);
	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public final ResponseEntity<Contact> create(@Valid @RequestBody ContactDTO contactDTO) {
		Contact contact = ((ContactService)getService()).create(contactDTO);
		return new ResponseEntity<>(contact, HttpStatus.CREATED);
	}
	
	@Override
	protected BaseService<Contact> getService() {
		return contactService;
	}
}
