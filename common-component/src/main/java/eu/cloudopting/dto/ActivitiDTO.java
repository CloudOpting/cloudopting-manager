package eu.cloudopting.dto;

/**
 * Created by danielpo on 09/07/2015.
 */
public class ActivitiDTO {
    String applicationId;
    String processInstanceId;

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
}
