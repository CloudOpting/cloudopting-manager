package eu.cloudopting.dto;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.User;

public class MultipleUploadDTO {
    String type;    
    String fileId;
    String idApp;
    String processId;
    Organizations org;
    User user;
    List<ImageDTO> images;
    
    public MultipleUploadDTO() {
    	images = new ArrayList<ImageDTO>();
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public List<ImageDTO> getImages() {
		return images;
	}

	public void setImages(List<ImageDTO> images) {
		this.images = images;
	}

	public void addImage(String name, InputStream is) {
		ImageDTO dto = new ImageDTO();
		dto.setName(name);
		dto.setInputStream(is);
		images.add(dto);
		
	}
    
}
