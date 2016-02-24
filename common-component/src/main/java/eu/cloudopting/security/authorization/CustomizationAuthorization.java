package eu.cloudopting.security.authorization;

public interface CustomizationAuthorization {

	public boolean hasWriteCustomizationPermission(Long customizationId);
}
