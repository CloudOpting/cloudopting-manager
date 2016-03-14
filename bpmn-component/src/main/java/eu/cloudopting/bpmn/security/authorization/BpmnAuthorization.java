package eu.cloudopting.bpmn.security.authorization;

public interface BpmnAuthorization {
	
	public boolean hasWriteApplicationPermission(String idApp);
	
	public boolean hasWriteCustomizationPermission(String idCustomization);
}
