package br.ufc.great.sysadmin.model;

import javax.persistence.Entity;

@Entity
public class Picture extends AbstractModel<Long>{
	private static final long serialVersionUID = 1L;
	private String name;
	private String path;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
