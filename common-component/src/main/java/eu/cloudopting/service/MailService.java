package eu.cloudopting.service;

import eu.cloudopting.domain.User;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;
    private String subject;
    private boolean isMultipart;
    private boolean isHtml;
    private String content;
    

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
    }

    public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMultipart(boolean isMultipart) {
		this.isMultipart = isMultipart;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void setContent(Map<String, Object> dataMap, Template template) {
		StringWriter writer = new StringWriter();
		try {
			template.process(dataMap, writer);
			this.content = writer.toString();
		} catch (TemplateException e) {
			log.warn("Error parsing the template, message is {}", e.getMessage());
		} catch (IOException e) {
			log.warn("Error parsing the template, message is {}", e.getMessage());
		}
	}
	
	public void sendEmail(String to) {
		sendEmail(to, this.subject, this.content, this.isMultipart, this.isHtml);
	}
	
	
	// CUSTOMIZATION
	// prendo path a jackrabbit che contiene il template
	//recupero il file, leggo il template, applico i dati e ottengo la stringa
	
	

	@Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        String content = "<html>" +
                "<body>" +
                "<p> Dear "+user.getFirstName()+", </p>" +
                "<p>Activation URL: "+baseUrl+"/#/activate?key="+user.getActivationKey()+"</p>" +
                "<p>Best regards,</p>" +
                "<p>CloudOpting team.</p>" +
                "</body>" +
                "</html>";
        String subject = "CloudOpting Catalog Activation Link";
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        String content = "<html>" +
                "<body>" +
                "<p> Dear "+user.getFirstName()+", </p>" +
                "<p>Reset password URL: "+baseUrl+"/#/reset/finish?key="+user.getResetKey()+"</p>" +
                "<p>Best regards,</p>" +
                "<p>CloudOpting team.</p>" +
                "</body>" +
                "</html>";
        String subject = "CloudOpting Catalog Reset Password Link";
        sendEmail(user.getEmail(), subject, content, false, true);
    }
}
