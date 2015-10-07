package eu.cloudopting.dto;

import java.io.Serializable;

/**
 * Created by danielpo on 09/07/2015.
 */
public class ApplicationDTO implements Serializable{


    private String applicationName;
    private String applicationDescription;
    private String applicationToscaName;
    private Long id;
    String status;
    String type;
    String name;
    String value;
    String processId;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "applicationName='" + applicationName + '\'' +
                ", applicationDescription='" + applicationDescription + '\'' +
                '}';
    }

	public String getApplicationToscaName() {
		return applicationToscaName;
	}

	public void setApplicationToscaName(String applicationToscaName) {
		this.applicationToscaName = applicationToscaName;
	}

}

