package br.ufc.great.sysadmin.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Notes extends AbstractModel<Long>{
	private static final long serialVersionUID = 1L;	
	@Column(length=255)
	private String description;
	
	public Notes() {
	}
		
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

}
