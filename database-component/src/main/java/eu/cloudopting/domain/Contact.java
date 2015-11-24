package eu.cloudopting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import eu.cloudopting.events.api.entity.BaseEntity;

@Entity
@Table(name = "CONTACT")
public class Contact implements BaseEntity {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @NotNull
    @Email
    @Size(min = 1, max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;
    
    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Size(max = 1000)
    @Column(name = "message", length = 1000)
    private String message;
    
	@Size(max = 100)
    @Column(name = "company_name", length = 100)
    private String companyName;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
