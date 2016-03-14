package eu.cloudopting.domain.util;

public enum OrganizationStatusEnum {
	Pending(1),
	Validated(2),
	Retired(3);
	
	private int id;
	
	OrganizationStatusEnum(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
