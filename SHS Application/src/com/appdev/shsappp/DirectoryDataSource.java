package com.appdev.shsappp;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DirectoryDataSource {


	

	private SQLiteDatabase database;
	private DirectoryDBHelper dbHelper; 
	private String[] allColumns = { 
			DirectoryDBHelper.COLUMN_ID,
			DirectoryDBHelper.COLUMN_NAME,
			DirectoryDBHelper.COLUMN_ROLE,
			DirectoryDBHelper.COLUMN_EXTENSION,
			DirectoryDBHelper.COLUMN_WEBSITE,
			DirectoryDBHelper.COLUMN_EMAIL,
			DirectoryDBHelper.COLUMN_TAGS,
			DirectoryDBHelper.COLUMN_SAVED
	};
	private String[] allColumnsSched = { 
			DirectoryDBHelper.ID,
			DirectoryDBHelper.START_TIME,
			DirectoryDBHelper.END_TIME,
			DirectoryDBHelper.PERIOD_NAME,
			DirectoryDBHelper.DAY,
			DirectoryDBHelper.SAVED
	};
	private Context context;
	public List<Contact> staff;
	public List<Schedule> scheds;

	public DirectoryDataSource(Context c) {
		this.context = c;
		dbHelper = new DirectoryDBHelper(context);
		
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		staff = getAllContacts();
		scheds= getAllSchedules();
	}

	public void close() {
		dbHelper.close();
	}
	public Schedule createSched(String starttime, String endtime, String periodname, String day) {
		ContentValues values = new ContentValues();
		values.put(DirectoryDBHelper.START_TIME,starttime);
		values.put(DirectoryDBHelper.END_TIME, endtime);
		values.put(DirectoryDBHelper.PERIOD_NAME, periodname);
		values.put(DirectoryDBHelper.DAY, day);
		values.put(DirectoryDBHelper.SAVED, String.valueOf(false));
		long insertId = database.insert(DirectoryDBHelper.TABLE_SCHEDULE, null,
				values);
		Cursor cursor = database.query(DirectoryDBHelper.TABLE_SCHEDULE,
				allColumnsSched, DirectoryDBHelper.ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Schedule newSchedule = cursorToSchedule(cursor);
		cursor.close();
		return newSchedule;
	}
	
	public Contact createContact(String name, String role, String extension, 
			String website, String email, String tags) {
		ContentValues values = new ContentValues();
		values.put(DirectoryDBHelper.COLUMN_NAME, name);
		values.put(DirectoryDBHelper.COLUMN_ROLE, role);
		values.put(DirectoryDBHelper.COLUMN_EXTENSION, extension);
		values.put(DirectoryDBHelper.COLUMN_WEBSITE, website);
		values.put(DirectoryDBHelper.COLUMN_EMAIL, email);
		values.put(DirectoryDBHelper.COLUMN_TAGS, tags);
		values.put(DirectoryDBHelper.COLUMN_SAVED, String.valueOf(false));
		long insertId = database.insert(DirectoryDBHelper.TABLE_STAFF, null,
				values);
		Cursor cursor = database.query(DirectoryDBHelper.TABLE_STAFF,
				allColumns, DirectoryDBHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Contact newContact = cursorToContact(cursor);
		cursor.close();
		return newContact;
	}
	
	public void setSaved(int id, boolean saved) {
		ContentValues values = new ContentValues();
		values.put(DirectoryDBHelper.COLUMN_SAVED, String.valueOf(saved));
		database.update(DirectoryDBHelper.TABLE_STAFF, values, DirectoryDBHelper.COLUMN_ID + "=" + id, null);
	}

	public List<Contact> getAllContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor cursor = database.query(DirectoryDBHelper.TABLE_STAFF,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contact contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		return contacts;
	}
	public List<Schedule> getAllSchedules() {
		List<Schedule> schedules = new ArrayList<Schedule>();
		Cursor cursor = database.query(DirectoryDBHelper.TABLE_SCHEDULE,
				allColumnsSched, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Schedule schedule=cursorToSchedule(cursor);
			schedules.add(schedule);
			cursor.moveToNext();
		}
		cursor.close();
		return schedules;
	}
	public List<Contact> getFilterRoleContacts(String role) {
		if(staff.size() == 0) {
			staff = getAllContacts();
		}
		List<Contact> filtered = new ArrayList<Contact>();
		for(Contact c : staff) {
			if(c.role.equals(role))
				filtered.add(c);
		}
		return filtered;
	}
	public List<Schedule> getFilterDaySchudules(String day) {
		if(scheds.size() == 0) {
			scheds = getAllSchedules();
		}
		List<Schedule> filtered = new ArrayList<Schedule>();
		for(Schedule s : scheds) {
			if(s.day.equals(day))
				filtered.add(s);
		}
		return filtered;
	}
	
	
	public List<Contact> getFilterSavedContacts() {
		if(staff.size() == 0) {
			staff = getAllContacts();
		}
		List<Contact> filtered = new ArrayList<Contact>();
		for(Contact c : staff) {
			if(c.saved)
				filtered.add(c);
		}
		return filtered;
	}

	public static Contact cursorToContact(Cursor c) {
		Contact contact = new Contact();
		contact.id = c.getInt(0);
		contact.name = c.getString(1);
		contact.role = c.getString(2);
		contact.extension = c.getString(3);
		contact.website = c.getString(4);
		contact.email = c.getString(5);
		StringTokenizer st = new StringTokenizer(c.getString(6));
		contact.saved = Boolean.parseBoolean(c.getString(7));
		while(st.hasMoreTokens()) {
			contact.tags.add(st.nextToken());
		}
		return contact;
	}
	public static Schedule cursorToSchedule(Cursor c) {
		Schedule schedule = new Schedule();
		schedule.id = c.getInt(0);
		schedule.timestart = c.getString(1);
		schedule.timefinish = c.getString(2);
		schedule.periodname = c.getString(3);
		schedule.day = c.getString(4);
		schedule.saved = Boolean.parseBoolean(c.getString(5));
		return schedule;
	}
	
	public void loadDatabase(boolean isNetwork) {
		database = dbHelper.getWritableDatabase();
		staff = getAllContacts();
		scheds= getAllSchedules();
		if(staff.size()==0 || scheds.size()==0 || isNetwork){
		dbHelper.onDelete(database);
		Parse.initialize(context,
				"mBeDrmdeuRATh3rO7CqbTZMYKcXkuSrCKPEkPFDG",
				"VoIiZFddiKtfH9i7iz5jyQMsT9H45KgnDUOtEDo2");
		
		ParseQuery<ParseObject> querier = ParseQuery.getQuery("Staff");
		querier.setLimit(1000);
		querier.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> ler, com.parse.ParseException e) {
				
				if (e == null) {
					for (int i = 0; i < ler.size(); i++) {
						String name=ler.get(i).getString("Name");
						String role = ler.get(i).getString("Type");
						String extension = ler.get(i).getString("Extension");
						String website = ler.get(i).getString("Website");
						String email = ler.get(i).getString("Email");
						String tags = new String();
						//Log.d("name","namer"+name);
						createContact(name, role,extension, website, email, tags);
					} 
					
				}
				
			}

		});
		
		//updatePercent((float)100/6);
		ParseQuery<ParseObject> query_sched = ParseQuery.getQuery("Monday");
		query_sched.orderByAscending("endTime");
		query_sched.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> l, com.parse.ParseException e) {
				
				if (e == null) {
					for (int i = 0; i < l.size(); i++) {
						String times=l.get(i).getString("time");
								String[] splitTime = times.split("-");
								
						String startTime=splitTime[0];
						String endTime=splitTime[1];
						String periodname=l.get(i).getString("period");
						createSched(startTime, endTime,periodname, "Monday");
					} 
					
				}
				
			}

		});
		//updatePercent((float)(100/6)*2);
		ParseQuery<ParseObject> query_sched2 = ParseQuery.getQuery("Tuesday");
		query_sched2.orderByAscending("endTime");
		query_sched2.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> l, com.parse.ParseException e) {
				
				if (e == null) {
					for (int i = 0; i < l.size(); i++) {
						String times=l.get(i).getString("time");
								String[] splitTime = times.split("-");
								
						String startTime=splitTime[0];
						String endTime=splitTime[1];
						String periodname=l.get(i).getString("period");
						createSched(startTime, endTime,periodname, "Tuesday");
					} 
					
				}
				
			}

		});
		//updatePercent((float)(100/6)*3);
		ParseQuery<ParseObject> query_sched3 = ParseQuery.getQuery("Wednesday");
		query_sched3.orderByAscending("endTime");
		query_sched3.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> l, com.parse.ParseException e) {
				
				if (e == null) {
					for (int i = 0; i < l.size(); i++) {
						String times=l.get(i).getString("time");
								String[] splitTime = times.split("-");
								
						String startTime=splitTime[0];
						String endTime=splitTime[1];
						String periodname=l.get(i).getString("period");
						createSched(startTime, endTime,periodname, "Wednesday");
					} 
					
				}
				
			}

		});
		//updatePercent((float)(100/6)*4);
		ParseQuery<ParseObject> query_sched4 = ParseQuery.getQuery("Thursday");
		query_sched4.orderByAscending("endTime");
		query_sched4.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> l, com.parse.ParseException e) {
				
				if (e == null) {
					for (int i = 0; i < l.size(); i++) {
						String times=l.get(i).getString("time");
								String[] splitTime = times.split("-");
								
						String startTime=splitTime[0];
						String endTime=splitTime[1];
						String periodname=l.get(i).getString("period");
						createSched(startTime, endTime,periodname, "Thursday");
					} 
					
				}
				
			}

		});
		//updatePercent((float)(100/6)*5);
		ParseQuery<ParseObject> query_sched5 = ParseQuery.getQuery("Friday");
		query_sched5.orderByAscending("endTime");
		query_sched5.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> l, com.parse.ParseException e) {
				
				if (e == null) {
					for (int i = 0; i < l.size(); i++) {
						String times=l.get(i).getString("time");
								String[] splitTime = times.split("-");
								
						String startTime=splitTime[0];
						String endTime=splitTime[1];
						String periodname=l.get(i).getString("period");
						createSched(startTime, endTime,periodname, "Friday");
					} 
					
				}
				
			}

		});
		//updatePercent(100);

		}else {

		}
		
	}

	
}
