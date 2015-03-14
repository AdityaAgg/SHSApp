package com.appdev.shsappp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DirectoryDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_STAFF = "staff";
	public static final String TABLE_SCHEDULE="schedule";
	public static final String START_TIME="start";
	public static final String END_TIME="end";
	public static final String PERIOD_NAME="period";
	public static final String DAY="day";
	public static final String ID = "_id";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ROLE = "role";
	public static final String COLUMN_EXTENSION = "extension";
	public static final String COLUMN_WEBSITE = "website";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_TAGS = "tags";
	public static final String COLUMN_SAVED = "saved";
	public static final String SAVED = "saved";

	private static final String DATABASE_NAME = "staff.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_SCHED = "create table " + TABLE_SCHEDULE + 
			"(" + ID + " integer primary key autoincrement, "
			+ START_TIME + " text not null, "
			+ END_TIME + " text not null, " 
			+ PERIOD_NAME+ " text not null, "
			+ DAY + " text not null, "
			+ SAVED + " text not null);";
	private static final String DATABASE_CREATE = "create table " + TABLE_STAFF + 
			"(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_ROLE + " text not null, " 
			+ COLUMN_EXTENSION + " text not null, "
			+ COLUMN_WEBSITE + " text not null, "
			+ COLUMN_EMAIL + " text not null, "
			+ COLUMN_TAGS + " text, "
			+ COLUMN_SAVED + " text not null);";
	public DirectoryDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE_SCHED);
		
	}
	public void onDelete(SQLiteDatabase db) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF);
		   db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
		   onCreate(db);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DirectoryDBHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
		    onCreate(db);
	}

}
