package eu.cloudopting.tosca.security.authorization;

public interface ToscaAuthorization {

	public boolean hasReadCustomizationPermission(String idApp);
	
	public boolean hasWriteCustomizationPermission(String idApp);
}
