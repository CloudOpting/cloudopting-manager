package eu.cloudopting.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class ContactDTO implements Serializable{
	
    @NotNull
    @Size(min = 1, max = 100)
    private String name;
    
    @NotNull
    @Email
    @Size(min = 1, max = 100)
    private String email;
    
    @Size(max = 20)
    private String phone;
    
    @Size(max = 1000)
    private String message;
    
	@Size(max = 100)
    private String companyName;

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
