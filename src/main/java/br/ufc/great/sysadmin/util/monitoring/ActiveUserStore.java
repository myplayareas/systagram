package br.ufc.great.sysadmin.util.monitoring;

import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore {
	 
    public List<String> users;
 
    public ActiveUserStore() {
        users = new ArrayList<String>();
    }

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
 
}

//Tecnical details about spring security session: https://docs.spring.io/spring-security/site/docs/3.0.x/reference/technical-overview.html
// Spring security reference: https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/
