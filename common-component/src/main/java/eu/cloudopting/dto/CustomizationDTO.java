package eu.cloudopting.dto;

import java.util.Date;

/**
 * DTO Object for customizations
 */
public class CustomizationDTO {
    String idApp;
    String user;
    String organization;
    String type;
    String customizationId;
    String status;
    Boolean payService;
    Boolean payPlatform;
    Boolean isTrial;
    Date trialEndDate;
    
    public String getCustomizationId() {
        return customizationId;
    }

    public void setCustomizationId(String customizationId) {
        this.customizationId = customizationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdApp() {
        return idApp;
    }

    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public Boolean getPayService() {
		return payService;
	}

	public void setPayService(Boolean payService) {
		this.payService = payService;
	}

	public Boolean getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(Boolean payPlatform) {
		this.payPlatform = payPlatform;
	}

	public Boolean getIsTrial() {
		return isTrial;
	}

	public void setIsTrial(Boolean isTrial) {
		this.isTrial = isTrial;
	}

	public Date getTrialEndDate() {
		return trialEndDate;
	}

	public void setTrialEndDate(Date trialEndDate) {
		this.trialEndDate = trialEndDate;
	}
}
