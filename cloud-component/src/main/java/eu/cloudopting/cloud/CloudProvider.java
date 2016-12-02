package eu.cloudopting.cloud;

public enum CloudProvider {

	DIGITALOCEAN("digitalocean2");
	
	private String jcloudsName;
	
	CloudProvider(String jcloudsName){
		this.jcloudsName = jcloudsName;
	}

	public String getJcloudsName() {
		return jcloudsName;
	}
}
