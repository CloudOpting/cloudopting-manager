package eu.cloudopting.bpmn;

/**
 * Constants from service.constants.js
 * @author guido
 *
 */
public enum BpmnServiceConstants {
	SERVICE_SEPARATOR("/"), 
	SERVICE_ROLE_PUBLISHER("ROLE_PUBLISHER"),
	SERVICE_ROLE_ADMIN("ROLE_ADMIN"),
	SERVICE_ROLE_OPERATOR("ROLE_OPERATOR"),
	SERVICE_ROLE_SUBSCRIBER("ROLE_SUBSCRIBER"),
	SERVICE_STATUS_UNFINISHED("UNFINISHED"),
	SERVICE_STATUS_UPLOADED("UPLOADED"),
	SERVICE_STATUS_DRAFT("DRAFT"),
	SERVICE_STATUS_READY_TO_PUBLISH("READY_TO_PUBLISH"),
	SERVICE_STATUS_PUBLISHED("PUBLISHED"),
	SERVICE_STORAGE_CURRENT_APP("currentApplication"),
	SERVICE_STORAGE_CURRENT_INSTANCE_ID("currentInstanceId"),
	SERVICE_STORAGE_ACTIVITI("activiti"),
	SERVICE_FILE_TYPE_PROMO_IMAGE("promoimage"),
	SERVICE_FILE_TYPE_CONTENT_LIBRARY("contentlibrary"),
	SERVICE_FILE_TYPE_TOSCA_ARCHIVE("toscaarchive");
	
	private final String constantValue;
	
	@Override
    public String toString() {
        return constantValue;
    }

	private BpmnServiceConstants(String constantValue) {
		this.constantValue = constantValue;
	}
	
	
}
