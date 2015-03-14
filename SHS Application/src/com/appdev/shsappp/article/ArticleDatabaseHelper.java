package com.appdev.shsappp.article;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ArticleDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "articles.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_CACHED = "cached";
	public static final String TABLE_SECTIONS = "sections";
	
	public static final String COLUMN_ID				= "_id";
	public static final String COLUMN_NODE_ID 			= "nid";
	public static final String COLUMN_TITLE 			= "title";
	public static final String COLUMN_STORY_TYPE 		= "story_type";
	public static final String COLUMN_IS_SPOTLIGHT		= "is_spotlight";
	public static final String COLUMN_AUTHOR 			= "author";
	public static final String COLUMN_DATE 				= "date"; 
	public static final String COLUMN_SUMMARY 			= "summary";
	public static final String COLUMN_STORY 			= "story";
	public static final String COLUMN_IMAGE_URL			= "image_url";
	public static final String COLUMN_IMAGE_SUBTITLE	= "subtitle";
	
	public static final String COLUMN_SECTION_NIDS		= "section_nids";
	
	private static final String DATABASE_CREATE = "create table " + TABLE_CACHED + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NODE_ID + " integer, "
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_STORY_TYPE + " text not null, "
			+ COLUMN_IS_SPOTLIGHT + " boolean, "
			+ COLUMN_AUTHOR + " text not null, " 
			+ COLUMN_DATE + " text not null, " 
			+ COLUMN_SUMMARY + " text, "
			+ COLUMN_STORY + " text, "
			+ COLUMN_IMAGE_URL + " text," 
			+ COLUMN_IMAGE_SUBTITLE + " text);";
	
	private static final String DATABASE_CREATE_SECTIONS = "create table " + TABLE_SECTIONS + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_SECTION_NIDS + " text not null);";
			
	
	public ArticleDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE_SECTIONS);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ArticleDatabaseHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHED);
		    onCreate(db);
	}
}
