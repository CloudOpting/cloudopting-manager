package eu.cloudopting.bpmn.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class BasicProcessInfo {
	@Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String processDefinitionId;
	
	@Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
	
	@Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String key;
	
	@NotNull
    private int version;
	
	@Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String deploymentId;

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public int getVersion() {
		return version;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public BasicProcessInfo(String processDefinitionId, String name,
			String key, int version, String deploymentId) {
		super();
		this.processDefinitionId = processDefinitionId;
		this.name = name;
		this.key = key;
		this.version = version;
		this.deploymentId = deploymentId;
	}

	@Override
	public String toString() {
		return "BasicProcessInfo [processDefinitionId=" + processDefinitionId
				+ ", name=" + name + ", key=" + key + ", version=" + version
				+ ", deploymentId=" + deploymentId + "]";
	}

}
