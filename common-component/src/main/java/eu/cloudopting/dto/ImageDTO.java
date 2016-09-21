package eu.cloudopting.dto;

import java.io.InputStream;

public class ImageDTO {
	String name;
	InputStream inputStream;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
