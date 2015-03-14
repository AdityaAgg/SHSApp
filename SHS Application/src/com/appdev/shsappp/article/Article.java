package com.appdev.shsappp.article;

import java.io.Serializable;

public class Article implements Serializable, Comparable<Article> {
	
	public static String[] storyTypes = {"news", "sports", "features", "opinion", "columns", "spotlight"};
	private static final long serialVersionUID = 1L;
	
	public static final int ID_UNASSIGNED = -1;
	
	public int id;
	public int nid;
	
	public volatile boolean cached = false;
	public volatile boolean parsed = false;
	
	public String title, storyType, author, date, story, summary, imageUrl, imageSubtitle;
	public boolean isSpotlight;
	public SerializableBitmap image;
	
	public boolean hasImage() {
		return !imageUrl.equals("");
	}

	@Override
	public int compareTo(Article other) {
		return other.nid - nid;
	}
}
