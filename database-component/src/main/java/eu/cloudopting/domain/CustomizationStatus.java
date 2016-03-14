package eu.cloudopting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import eu.cloudopting.events.api.entity.BaseEntity;

@Entity
@Table(name = "CUSTOMIZATION_STATUS")
@SequenceGenerator(name = "customizationStatusGen", sequenceName = "customization_status_id_seq")
public class CustomizationStatus implements BaseEntity {

	private static final long serialVersionUID = 7190307209136905138L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customizationStatusGen")
    private Long id;
    
    @Column(name = "status", length = 50, unique = true)
    @NotNull
    private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
