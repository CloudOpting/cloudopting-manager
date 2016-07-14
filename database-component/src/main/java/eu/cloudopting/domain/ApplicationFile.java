package eu.cloudopting.domain;

public class ApplicationFile {
	
	public static final String TYPE_PROMO = "promoimage";
	public static final String TYPE_TOSCA = "toscaarchive";
	public static final String TYPE_CONTENT = "contentlibrary";
	
	private String filePath;
	private String fileType;
	
	public ApplicationFile(String filePath, String fileType) {
		super();
		this.filePath = filePath;
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
