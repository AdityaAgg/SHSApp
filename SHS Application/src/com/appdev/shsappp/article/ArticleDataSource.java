package com.appdev.shsappp.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ArticleDataSource {
	private SQLiteDatabase database;
	private ArticleDatabaseHelper dbHelper;

	private String[] allColumns = {
			ArticleDatabaseHelper.COLUMN_ID,
			ArticleDatabaseHelper.COLUMN_NODE_ID,
			ArticleDatabaseHelper.COLUMN_TITLE,
			ArticleDatabaseHelper.COLUMN_STORY_TYPE,
			ArticleDatabaseHelper.COLUMN_IS_SPOTLIGHT,
			ArticleDatabaseHelper.COLUMN_AUTHOR,
			ArticleDatabaseHelper.COLUMN_DATE,
			ArticleDatabaseHelper.COLUMN_SUMMARY,
			ArticleDatabaseHelper.COLUMN_STORY,
			ArticleDatabaseHelper.COLUMN_IMAGE_URL,
			ArticleDatabaseHelper.COLUMN_IMAGE_SUBTITLE
	};

	private String[] allColumnsSections = {
			ArticleDatabaseHelper.COLUMN_ID,
			ArticleDatabaseHelper.COLUMN_SECTION_NIDS
	};

	public Context context;

	public ArticleDataSource(Context context) {
		this.context = context;
		dbHelper = new ArticleDatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Article cursorToArticle(Cursor cursor) {
		Article article = new Article();
		article.id = cursor.getInt(0);
		article.nid = cursor.getInt(1);
		article.title = cursor.getString(2);
		article.storyType = cursor.getString(3);
		article.isSpotlight = Boolean.parseBoolean(cursor.getString(4));
		article.author = cursor.getString(5);
		article.date = cursor.getString(6);
		article.summary = cursor.getString(7);
		article.story = cursor.getString(8);
		article.imageUrl = cursor.getString(9);
		article.imageSubtitle = cursor.getString(10);
		article.cached = true;
		return article;
	}

	public void addSection(String section, int[] nids) {
		String representation = section;
		if(nids != null) {
			for(int i = 0; i < nids.length; i++) {
				representation += " " + nids[i];
			}
			ContentValues values = new ContentValues();
			values.put(ArticleDatabaseHelper.COLUMN_SECTION_NIDS, representation);
			database.insert(ArticleDatabaseHelper.TABLE_SECTIONS, null,
					values);
		}
	}

	public void clearSections() {
		database.execSQL("DELETE FROM " + ArticleDatabaseHelper.TABLE_SECTIONS);
	}

	public void addArticle(Article article) {
		ContentValues values = new ContentValues();
		values.put(ArticleDatabaseHelper.COLUMN_NODE_ID, String.valueOf(article.nid));
		values.put(ArticleDatabaseHelper.COLUMN_TITLE, article.title);
		values.put(ArticleDatabaseHelper.COLUMN_STORY_TYPE, article.storyType);
		values.put(ArticleDatabaseHelper.COLUMN_IS_SPOTLIGHT, article.isSpotlight);
		values.put(ArticleDatabaseHelper.COLUMN_AUTHOR, article.author);
		values.put(ArticleDatabaseHelper.COLUMN_DATE, article.date);
		values.put(ArticleDatabaseHelper.COLUMN_SUMMARY, article.summary);
		values.put(ArticleDatabaseHelper.COLUMN_STORY, article.story);
		values.put(ArticleDatabaseHelper.COLUMN_IMAGE_URL, article.imageUrl);
		values.put(ArticleDatabaseHelper.COLUMN_IMAGE_SUBTITLE, article.imageSubtitle);
		database.insert(ArticleDatabaseHelper.TABLE_CACHED, null,
				values);
	}

	public void deleteArticle(Article article) {
		database.delete(ArticleDatabaseHelper.TABLE_CACHED, ArticleDatabaseHelper.COLUMN_ID + "=" + article.id, null);
	}

	public ArrayList<Article> getCache() {
		ArrayList<Article> cache = new ArrayList<Article>();
		Cursor cursor = database.query(ArticleDatabaseHelper.TABLE_CACHED, allColumns, null, null, null, 
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Article article = cursorToArticle(cursor);
			article.cached = true;
			cache.add(article);
			cursor.moveToNext();
		}
		return cache;
	}

	public HashMap<String, int[]> getMap() {
		Cursor cursor = database.query(ArticleDatabaseHelper.TABLE_SECTIONS, allColumnsSections, null, null, null, 
				null, null);
		cursor.moveToFirst();
		HashMap<String, int[]> result = new HashMap<String, int[]>();
		while (!cursor.isAfterLast()) {
			String rep = cursor.getString(1);
			StringTokenizer tokens = new StringTokenizer(rep);
			String section = tokens.nextToken();
			ArrayList<Integer> temp = new ArrayList<Integer>();
			while(tokens.hasMoreTokens()) {
				temp.add(Integer.parseInt(tokens.nextToken()));
			}
			int[] nids = new int[temp.size()];
			for(int i = 0; i < temp.size(); i++) {
				nids[i] = temp.get(i);
			}
			result.put(section, nids);
			cursor.moveToNext();
		}
		return result;
	}

	public void clearCache() {
		database.execSQL("DELETE FROM " + ArticleDatabaseHelper.TABLE_CACHED);
	}

}
