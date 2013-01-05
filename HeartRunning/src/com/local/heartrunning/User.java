package com.local.heartrunning;

import com.facebook.model.GraphUser;

public class User {
	
	private static User instance = null;
	private GraphUser facebook;
	
	protected User() {
		
	}
	
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}
	
	public void addFacebook(GraphUser u) {
		facebook = u;
	}
	
	public GraphUser getFacebook() {
		return facebook;
	}
}
