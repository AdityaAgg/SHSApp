package com.appdev.shsappp.webserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.SerializableBitmap;

public class WebServer {

	public static final HashMap<String, SerializableBitmap> imageCache = 
			new HashMap<String, SerializableBitmap>(); 

	public static final String SERVER_URL = "http://saratogafalcon.org/ws";
	public static final String TEST_SERVER_URL = "http://falcon7.miramontes.com/ws";
	
	public static final String REQUEST_TYPE = "request_type";
	public static final String GET_STORY_DETAILS = "get_story_details";
	public static final String GET_STORY_NIDS = "get_story_nids";
	public static final String SEARCH = "search";
	public static final String STORY_TYPE = "story_type";
	public static final String STORY_NUMBER = "story_number";
	public static final String TERMS = "terms";
	public static final String TEN_MOST_RECENT_STORY_NIDS = "ten_most_recent_story_nids";
	public static final String STORY_NID = "story_nid";
	
	public static String[] months = {"January", "February", "March", "April", "May", 
		"June", "July", "August", "September", "October" , "November", "December"
	};
	
	public static String parseDate(String date) {
		String year = date.substring(0, 4);
		String month = months[Integer.parseInt(date.substring(5, 7)) - 1];
		String day = date.substring(8, 10);
		return month + " " + day + ", " + year;
	}

	private static volatile boolean isConnected = true;
	private static volatile boolean isRunning = true;

	public static void initialize() {
		isRunning = true;
		(new Thread() {
			public void run() {
				while(isRunning) {
					try{
						URL myUrl = new URL(SERVER_URL);
						URLConnection connection = myUrl.openConnection();
						connection.setConnectTimeout(4000);
						connection.connect();
						isConnected = true;
					} catch (Exception e) {
						isConnected = false;
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
	}

	public static boolean isConnected() {
		return isConnected;
	}

	public static void close() {
		isRunning = false;
	}

	public static String parseHTML(String story) {
		StringBuilder minibuilder = new StringBuilder();
		for(int i = 0; i < story.length(); i++) {
			if(story.charAt(i) != '\t')
				minibuilder.append(story.charAt(i));
		}
		String clean = minibuilder.toString();
		StringBuilder spanbuilder = new StringBuilder();
		int toStart = clean.toString().lastIndexOf("<p>");
		if(toStart != -1) {
			spanbuilder.append(clean.substring(0, toStart));
			spanbuilder.append("<span>");
			spanbuilder.append(clean.substring(toStart + 3));
		} else {
			return clean;
		}
		return spanbuilder.toString();
	}

	public static Article getArticleWithNid(int nid) {
		Log.w("SHS Falcon: WebServer", "Getting Article with Nid " + nid);
		String feed = getWebFeed(SERVER_URL, (REQUEST_TYPE + "=" + GET_STORY_DETAILS), (STORY_NID + "=" + nid));
		if(feed == null || feed.equals("")) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(feed);
			Article article = new Article();
			article.id = Article.ID_UNASSIGNED;
			article.nid = nid;
			article.title = jsonObject.getString("title");
			article.storyType = jsonObject.getString("storyType");
			article.author = jsonObject.getString("author");
			article.author = "by " + article.author;
			article.date = jsonObject.getString("date");
			article.date = parseDate(article.date.substring(0, article.date.indexOf(" ")));
			article.summary = jsonObject.getString("summary");
			article.story = jsonObject.getString("story");
			article.imageUrl = jsonObject.getString("imageURL");
			article.image = getImage(article.imageUrl);
			article.imageSubtitle = jsonObject.getString("imageSubtitle");
			return article;
		} catch(Exception e) {
			Log.w("SHS Falcon: WebServer", "Failed to Parse Article " + nid);
			e.printStackTrace();
			return null;
		}
	}

	public static int[] getStoryNidsWithTerms(String query) {
		String feed = getWebFeed(SERVER_URL, (REQUEST_TYPE + "=" + SEARCH),
				(TERMS + "=" + query));
		try {
			JSONArray jsonArray = new JSONArray(feed);
			int[] nids = new int[jsonArray.length()];
			for(int i = 0; i < jsonArray.length(); i++) {
				nids[i] = jsonArray.getInt(i);
			}
			return nids;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int[] getStoryNids(String storyType, int number) {
		String feed = getWebFeed(SERVER_URL, (REQUEST_TYPE + "=" + GET_STORY_NIDS),
				(STORY_TYPE + "=" + storyType), (STORY_NUMBER + "=" + number));
		try {
			JSONArray jsonArray = new JSONArray(feed);
			int[] nids = new int[jsonArray.length()];
			for(int i = 0; i < jsonArray.length(); i++) {
				nids[i] = jsonArray.getInt(i);
			}
			return nids;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int[] getTenMostRecentStoryNids() {
		String feed = getWebFeed((REQUEST_TYPE + "=" + TEN_MOST_RECENT_STORY_NIDS));
		try {
			JSONArray jsonArray = new JSONArray(feed);
			int[] nids = new int[jsonArray.length()];
			for(int i = 0; i < jsonArray.length(); i++) {
				nids[i] = jsonArray.getInt(i);
			}
			return nids;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static SerializableBitmap getImage(String url) {
		SerializableBitmap image = imageCache.get(url);
		if(image != null) {
			return image;
		}
		try {
			Bitmap bitmap;

			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.connect();
			InputStream input = connection.getInputStream();

			bitmap = BitmapFactory.decodeStream(input);
			bitmap.setDensity(Bitmap.DENSITY_NONE);
			return new SerializableBitmap(bitmap);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getWebFeed(String url, String... rawParams) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for(int i = 0; i < rawParams.length; i++) {
			String name = rawParams[i].substring(0, rawParams[i].indexOf("=")).trim();
			String key = rawParams[i].substring(rawParams[i].indexOf("=") + 1).trim();
			nameValuePairs.add(new BasicNameValuePair(name, key)); 
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				return null;
			}
		} catch(Exception e) {
			Log.w("SHS Falcon: WebServer", "Failed to Pull Data");
		}
		return builder.toString();
	}

}
