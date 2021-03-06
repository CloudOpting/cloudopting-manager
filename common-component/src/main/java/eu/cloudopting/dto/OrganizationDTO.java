package eu.cloudopting.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.cloudopting.domain.OrganizationStatus;
import eu.cloudopting.domain.OrganizationTypes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationDTO implements Serializable{

	private Long id;

    private OrganizationTypes organizationType; //notnull

    private Date organizationCreation; //notnull server

    private Date organizationActivation; //notnull

    private Date organizationDecommission; //notnull

    private OrganizationStatus organizationStatus; //notnull client

    private String description; //notnull client

    private String organizationKey;

    private String organizationName; // client

    @Email
    @Size(max = 100)
    private String email;
    
    @Size(max = 300)
    private String contactRepresentative;
    
    @Size(max = 100)
    private String contactPhone;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrganizationTypes getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(OrganizationTypes organizationType) {
		this.organizationType = organizationType;
	}

	public Date getOrganizationCreation() {
		return organizationCreation;
	}

	public void setOrganizationCreation(Date organizationCreation) {
		this.organizationCreation = organizationCreation;
	}

	public Date getOrganizationActivation() {
		return organizationActivation;
	}

	public void setOrganizationActivation(Date organizationActivation) {
		this.organizationActivation = organizationActivation;
	}

	public Date getOrganizationDecommission() {
		return organizationDecommission;
	}

	public void setOrganizationDecommission(Date organizationDecommission) {
		this.organizationDecommission = organizationDecommission;
	}

	public OrganizationStatus getOrganizationStatus() {
		return organizationStatus;
	}

	public void setOrganizationStatus(OrganizationStatus organizationStatus) {
		this.organizationStatus = organizationStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrganizationKey() {
		return organizationKey;
	}

	public void setOrganizationKey(String organizationKey) {
		this.organizationKey = organizationKey;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactRepresentative() {
		return contactRepresentative;
	}

	public void setContactRepresentative(String contactRepresentative) {
		this.contactRepresentative = contactRepresentative;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
}

