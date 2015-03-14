package com.appdev.shsappp;
import java.io.Serializable;
import java.util.ArrayList;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String ROLE_ADMIN = "Administration";
	public static final String ROLE_TEACHER = "Teacher";
	public static final String ROLE_GUIDANCE = "Guidance";
	public static final String ROLE_MISC = "Miscellaneous";		
	
	public static final String[] categories = {
		"All" ,ROLE_ADMIN, ROLE_TEACHER, ROLE_GUIDANCE, ROLE_MISC
	};
	
	public int id;
	public String name, role, extension, website, email;
	public ArrayList<String> tags;
	public boolean saved;
	
	public Contact() {
		tags = new ArrayList<String>();
	}
	
	public String toString() {
		return name;
	}
}
