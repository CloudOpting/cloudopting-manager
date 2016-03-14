package eu.cloudopting.dto;

/**
 * Created by danielpo on 09/07/2015.
 */
public class ActivitiDTO {
    String applicationId;
    String processInstanceId;
    String customizationId;
    String jrPath;


    public String getCustomizationId() {
        return customizationId;
    }

    public void setCustomizationId(String customizationId) {
        this.customizationId = customizationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

	public String getJrPath() {
		return jrPath;
	}

	public void setJrPath(String jrPath) {
		this.jrPath = jrPath;
	}
}
