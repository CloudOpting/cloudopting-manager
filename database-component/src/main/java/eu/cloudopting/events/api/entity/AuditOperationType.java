package eu.cloudopting.events.api.entity;

/**
 * Lista tipurilor de operatii auditare in aplicatie
 * @author MihaiT
 */
public enum AuditOperationType {

    INSERT ("INSERT"),
    UPDATE ("UPDATE"),
    DELETE ("DELETE");

    private String name;

    private AuditOperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
