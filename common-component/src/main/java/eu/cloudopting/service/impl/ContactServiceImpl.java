package eu.cloudopting.service.impl;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Contact;
import eu.cloudopting.dto.ContactDTO;
import eu.cloudopting.events.api.service.AbstractService;
import eu.cloudopting.repository.ContactRepository;
import eu.cloudopting.service.ContactService;
import eu.cloudopting.service.MailService;

@Service
@Transactional
public class ContactServiceImpl extends AbstractService<Contact> implements ContactService {

	private static final String CLOUDOPTING_EMAIL = "cloudopting@gmail.com";
	
	@Inject
	private ContactRepository contactRepository;

    @Inject
    private MailService mailService;
    
	public ContactServiceImpl() {
		super(Contact.class);
	}

	@Override
	public Contact create(ContactDTO contactDTO) {
		Contact contact = new Contact();
		BeanUtils.copyProperties(contactDTO, contact);
		Contact savedContact = create(contact);
		sendMessageReceivedNotificationEmail(contactDTO);
		sendMessageSentNotificationEmail(contactDTO);
		return savedContact;
	}

	private void sendMessageReceivedNotificationEmail(ContactDTO contactDTO) {
		String content = "<html>" +
				"<body>" +
				"<p>Message details: </p>" +
				"<p>From: " + contactDTO.getName() + "</p>" +
				"<p>Email: " + contactDTO.getEmail() + "</p>" +
				"<p>Phone: " + contactDTO.getPhone() + "</p>" +
				"<p>Message: " + contactDTO.getMessage() + "</p>" +
				"<p>Company: " + contactDTO.getCompanyName() + "</p>" +
				"</body>" +
				"</html>";
		String subject = "New message received from " + contactDTO.getName();
		mailService.sendEmail(CLOUDOPTING_EMAIL, subject, content, false, true);
	}

	private void sendMessageSentNotificationEmail(ContactDTO contactDTO) {
		String content = "<html>" +
				"<body>" +
				"<p> Dear " + contactDTO.getName() + ", </p>" +
				"<p>Message:</p>" +
				"<p>" + contactDTO.getMessage() + "</p>" +
				"<p>has been sent.</p>" +
				"</body>" +
				"</html>";
		String subject = "Message sent";
		mailService.sendEmail(contactDTO.getEmail(), subject, content, false, true);
	}

	@Override
	protected PagingAndSortingRepository<Contact, Long> getDao() {
		return contactRepository;
	}

	@Override
	protected JpaSpecificationExecutor<Contact> getSpecificationExecutor() {
		return contactRepository;
	}
}
