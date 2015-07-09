package eu.cloudopting.dto;

import java.io.Serializable;

/**
 * Created by danielpo on 09/07/2015.
 */
public class ApplicationDTO implements Serializable{


    private String applicationName;
    private String applicationDescription;
    private Long id;

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


    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "applicationName='" + applicationName + '\'' +
                ", applicationDescription='" + applicationDescription + '\'' +
                '}';
    }
}

