package eu.cloudopting.dto;

import eu.cloudopting.domain.ApplicationMedia;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.Status;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by danielpo on 09/07/2015.
 */
public class ApplicationDTO implements Serializable{


    private String applicationName;
    private String applicationDescription;

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

