package eu.cloudopting.dto;

import java.util.Date;

/**
 * DTO Object for customizations
 */
public class CustomizationDTO {
	Long id;
	Long cloudAccountId;
	Long statusId;
    Boolean payService;
    Boolean payPlatform;
    Boolean isTrial;
    Date trialEndDate;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCloudAccountId() {
		return cloudAccountId;
	}
	public void setCloudAccountId(Long cloudAccountId) {
		this.cloudAccountId = cloudAccountId;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
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
