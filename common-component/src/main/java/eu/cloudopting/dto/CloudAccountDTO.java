package eu.cloudopting.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.cloudopting.domain.Providers;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudAccountDTO implements Serializable{

	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String apiKey;
	
	@NotNull
	private String secretKey;
	
	@NotNull
	private String endpoint;
	
	@NotNull
	private Providers provider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Providers getProvider() {
		return provider;
	}

	public void setProvider(Providers provider) {
		this.provider = provider;
	}
}
