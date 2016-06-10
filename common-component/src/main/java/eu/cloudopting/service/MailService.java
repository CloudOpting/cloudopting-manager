package eu.cloudopting.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
			log.warn("Error paring the template, message is {}", e.getMessage());
		} catch (IOException e) {
			log.warn("Error paring the template, message is {}", e.getMessage());
		}
	}

	public void sendEmail(String to) {
		sendEmail(to, this.subject, this.content, this.isMultipart, this.isHtml);
	}

	@Async
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
		log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
				isHtml, to, subject, content);

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
		String content = "<html>" + "<body>" + "<p> Hi " + user.getFirstName() + ", </p>"
				+ "<p>Thanks for joining CloudOpting!</p>"
				+ "<p>Please, click the following link to verify your email: <a href='"
				+ baseUrl + "/cloudopting/#/activate?key=" + user.getActivationKey() + "'>"+ 
				baseUrl + "/cloudopting/#/activate?key=" + user.getActivationKey() + "</a></p>" + "<p>Best regards,</p>"
				+ "<p>CloudOpting team.</p>" + "</body>" + "</html>";
		String subject = "CloudOpting Catalogue Activation Link";
		sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Async
	public void sendPasswordResetMail(User user, String baseUrl) {
		log.debug("Sending password reset e-mail to '{}'", user.getEmail());
		String content = "<html>" + "<body>" + "<p> Dear " + user.getFirstName() + ", </p>" + "<p>Reset password URL: "
				+ baseUrl + "/cloudopting/#/reset/finish?key=" + user.getResetKey() + "</p>" + "<p>Best regards,</p>"
				+ "<p>CloudOpting team.</p>" + "</body>" + "</html>";
		String subject = "CloudOpting Catalogue Reset Password Link";
		sendEmail(user.getEmail(), subject, content, false, true);
	}
	
	@Async
	public void sendPasswordChangeMail(User user, String baseUrl) {
		log.debug("Sending password change e-mail to '{}'", user.getEmail());
		String content = "<html>" + "<body>" + "<p> Dear " + user.getFirstName() + ", </p>" + "Your password have been changed successfully</p><p>Best regards,</p>"
				+ "<p>CloudOpting team.</p>" + "</body>" + "</html>";
		String subject = "CloudOpting Catalogue Password changed succesfully";
		sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Async
	public void sendTemplatedMail(String to, String subject, String template, HashMap<String, Object> templateData,
			boolean isMultipart, boolean isHtml) {
		log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and template={}", isMultipart,
				isHtml, to, subject, template);

		StringWriter writer = new StringWriter();

		try {
			Template tpl = new Template("mailtemplate", new StringReader(template), new Configuration());
			tpl.process(templateData, writer);
		} catch (TemplateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String content = ((StringWriter) writer).getBuffer().toString();
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

}
