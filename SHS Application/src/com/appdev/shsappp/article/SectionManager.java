package com.appdev.shsappp.article;
import java.util.HashMap;

import android.content.Context;

public class SectionManager {
	
	private static HashMap<String, int[]> sectionNids;
	
	public static void initialize(final Context context) {
		if(sectionNids == null) {
			ArticleDataSource source = new ArticleDataSource(context);
			source.open();
			sectionNids = source.getMap();
			source.close();
			System.out.println(sectionNids);
		}
	}
	
	public static int[] getNids(String section) {
		if(sectionNids != null) {
			return sectionNids.get(section);
		} 
		return null;
	}
	
	public static void update(String section, int[] nids) {
		sectionNids.put(section, nids);
	}
	
	public static void cache(final Context context) {
		ArticleDataSource source = new ArticleDataSource(context);
		source.open();
		source.clearSections();
		for(int i = 0; i < Article.storyTypes.length; i++) {
			String focus = Article.storyTypes[i];
			source.addSection(focus, sectionNids.get(focus));
		}
		source.close();
	}
	
}
