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
import java.util.Set;

@Configurable
@Entity
@Table(schema = "public",name = "organizations")
public class Organizations implements BaseEntity {

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("cloudAccountss", "organizationType", "applicationss", "customizationss", "tUsers").toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Organizations().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOrganizationses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Organizations o", Long.class).getSingleResult();
    }

	public static List<Organizations> findAllOrganizationses() {
        return entityManager().createQuery("SELECT o FROM Organizations o", Organizations.class).getResultList();
    }

	public static List<Organizations> findAllOrganizationses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Organizations o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Organizations.class).getResultList();
    }

	public static Organizations findOrganizations(Long id) {
        if (id == null) return null;
        return entityManager().find(Organizations.class, id);
    }

	public static List<Organizations> findOrganizationsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Organizations o", Organizations.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Organizations> findOrganizationsEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Organizations o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Organizations.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Organizations attached = Organizations.findOrganizations(this.id);
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
    public Organizations merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Organizations merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(mappedBy = "organizationId")
    private Set<Applications> applicationss;

	@OneToMany(mappedBy = "organizationId")
    private Set<CloudAccounts> cloudAccountss;

	@OneToMany(mappedBy = "customerOrganizationId")
    private Set<Customizations> customizationss;

	@OneToMany(mappedBy = "organizationId")
    private Set<User> tUsers;

	@ManyToOne
    @JoinColumn(name = "organization_type", referencedColumnName = "id", nullable = false)
    private OrganizationTypes organizationType;

	@Column(name = "organization_creation")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date organizationCreation;

	@Column(name = "organization_activation")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date organizationActivation;

	@Column(name = "organization_decommission")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date organizationDecommission;

	@Column(name = "status_id")
    @NotNull
    private Long statusId;

	@Column(name = "description", length = 500)
    @NotNull
    private String description;

    @Column(name = "organization_key", length = 300)
    private String organizationKey;

    @Column(name = "organization_name", length = 300)
    private String organizationName;

	public Set<Applications> getApplicationss() {
        return applicationss;
    }

	public void setApplicationss(Set<Applications> applicationss) {
        this.applicationss = applicationss;
    }

	public Set<CloudAccounts> getCloudAccountss() {
        return cloudAccountss;
    }

	public void setCloudAccountss(Set<CloudAccounts> cloudAccountss) {
        this.cloudAccountss = cloudAccountss;
    }

	public Set<Customizations> getCustomizationss() {
        return customizationss;
    }

	public void setCustomizationss(Set<Customizations> customizationss) {
        this.customizationss = customizationss;
    }

	public Set<User> getTUsers() {
        return tUsers;
    }

	public void setTUsers(Set<User> tUsers) {
        this.tUsers = tUsers;
    }

	public OrganizationTypes getOrganizationType() {
        return organizationType;
    }

	public void setOrganizationType(OrganizationTypes organizationType) {
        this.organizationType = organizationType;
    }

	public Date getOrganizationCreation() {
        return organizationCreation;
    }

	public void setOrganizationCreation(Date organizationCreation) {
        this.organizationCreation = organizationCreation;
    }

	public Date getOrganizationActivation() {
        return organizationActivation;
    }

	public void setOrganizationActivation(Date organizationActivation) {
        this.organizationActivation = organizationActivation;
    }

	public Date getOrganizationDecommission() {
        return organizationDecommission;
    }

	public void setOrganizationDecommission(Date organizationDecommission) {
        this.organizationDecommission = organizationDecommission;
    }

	public Long getStatusId() {
        return statusId;
    }

	public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

	public String getDescription() {
        return description;
    }

	public void setDescription(String description) {
        this.description = description;
    }


    public String getOrganizationKey() {
        return organizationKey;
    }

    public void setOrganizationKey(String organizationKey) {
        this.organizationKey = organizationKey;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
}
