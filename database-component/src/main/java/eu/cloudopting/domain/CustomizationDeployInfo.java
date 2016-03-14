package eu.cloudopting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import eu.cloudopting.events.api.entity.BaseEntity;

@Entity
@Table(name = "CUSTOMIZATION_DEPLOY_INFO")
@SequenceGenerator(name = "customizationDeployInfoGen", sequenceName = "customization_deploy_info_id_seq")
public class CustomizationDeployInfo implements BaseEntity {

	private static final long serialVersionUID = 6614644833309609019L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customizationDeployInfoGen")
    private Long id;
	
    @Column(name = "hostname")
    private String hostname;
    
    @Column(name = "fqdn")
    private String fqdn;
    
    @Column(name = "internal_ip")
    private String internalIp;
  
    @Column(name = "public_ip")
    private String publicIp;
    
    @ManyToOne
    @JoinColumn(name = "customization_id", referencedColumnName = "id")
    private Customizations customization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public Customizations getCustomization() {
		return customization;
	}

	public void setCustomization(Customizations customization) {
		this.customization = customization;
	}
}
