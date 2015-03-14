package com.appdev.shsappp;
import java.io.Serializable;

public class Schedule implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	
	public int id;
	public String timestart, timefinish, periodname,day;
	public boolean saved;
	

	
	public String toString() {
		return periodname;
	}
}
