package eu.cloudopting.dto;

import java.io.InputStream;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.User;

/**
 * DTO object for upload
 */
public class UploadDTO {
    String name;
    String type;
    InputStream file;
    String fileId;
    String idApp;
    String processId;
    Organizations org;
    User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getIdApp() {
        return idApp;
    }

    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

	public Organizations getOrg() {
		return org;
	}

	public void setOrg(Organizations org) {
		this.org = org;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
