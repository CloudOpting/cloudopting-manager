package eu.cloudopting.domain;

import eu.cloudopting.events.api.entity.BaseEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Configurable
@Entity
@Table(schema = "public",name = "customizations")

public class Customizations implements BaseEntity {

	@ManyToOne
    @JoinColumn(name = "customer_organization_id", referencedColumnName = "id")
    private Organizations customerOrganizationId;

    @ManyToOne
    @JoinColumn(name = "cloud_account_id", referencedColumnName = "id")
    private CloudAccounts cloudAccount;

	@Column(name = "application_id")
    @NotNull
    private Long applicationId;

	@Column(name = "customization_tosca_file")
    @NotNull
    private String customizationToscaFile;

	@Column(name = "customization_creation")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date customizationCreation;

	@Column(name = "customization_activation")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date customizationActivation;

	@Column(name = "customization_decommission")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date customizationDecommission;

	@Column(name = "status_id")
    @NotNull
    private Long statusId;

	@Column(name = "process_id", length = 64)
    private String processId;
	
	//See: http://stackoverflow.com/questions/28588311/correct-jpa-annotation-for-postgresqls-text-type-without-hibernate-annotations
	//and  http://stackoverflow.com/questions/3868096/jpa-how-do-i-persist-a-string-into-a-database-field-type-mysql-text
	//Executed the following SQL Command on the db: ALTER TABLE customizations ADD COLUMN customization_form_value text;
//	@Lob
	@Column(name = "customization_form_value")
    private String customizationFormValue;

	public Organizations getCustomerOrganizationId() {
        return customerOrganizationId;
    }

	public void setCustomerOrganizationId(Organizations customerOrganizationId) {
        this.customerOrganizationId = customerOrganizationId;
    }

	public Long getApplicationId() {
        return applicationId;
    }

	public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

	public String getCustomizationToscaFile() {
        return customizationToscaFile;
    }

	public void setCustomizationToscaFile(String customizationToscaFile) {
        this.customizationToscaFile = customizationToscaFile;
    }

	public Date getCustomizationCreation() {
        return customizationCreation;
    }

	public void setCustomizationCreation(Date customizationCreation) {
        this.customizationCreation = customizationCreation;
    }

	public Date getCustomizationActivation() {
        return customizationActivation;
    }

	public void setCustomizationActivation(Date customizationActivation) {
        this.customizationActivation = customizationActivation;
    }

	public Date getCustomizationDecommission() {
        return customizationDecommission;
    }

	public void setCustomizationDecommission(Date customizationDecommission) {
        this.customizationDecommission = customizationDecommission;
    }

	public Long getStatusId() {
        return statusId;
    }

	public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

	public String getProcessId() {
        return processId;
    }

	public void setProcessId(String processId) {
        this.processId = processId;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("applicationId", "statusId", "customerOrganizationId").toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Customizations().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCustomizationses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Customizations o", Long.class).getSingleResult();
    }

	public static List<Customizations> findAllCustomizationses() {
        return entityManager().createQuery("SELECT o FROM Customizations o", Customizations.class).getResultList();
    }

	public static List<Customizations> findAllCustomizationses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Customizations o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Customizations.class).getResultList();
    }

	public static Customizations findCustomizations(Long id) {
        if (id == null) return null;
        return entityManager().find(Customizations.class, id);
    }

	public static List<Customizations> findCustomizationsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Customizations o", Customizations.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Customizations> findCustomizationsEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Customizations o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Customizations.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Customizations attached = Customizations.findCustomizations(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Customizations merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Customizations merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public CloudAccounts getCloudAccount() {
        return cloudAccount;
    }

    public void setCloudAccount(CloudAccounts cloudAccount) {
        this.cloudAccount = cloudAccount;
    }

	public String getCustomizationFormValue() {
		return customizationFormValue;
	}

	public void setCustomizationFormValue(String customizationFormValue) {
		this.customizationFormValue = customizationFormValue;
	}
}
