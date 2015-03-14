package com.appdev.shsappp.article;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import com.appdev.shsappp.webserver.WebServer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ArticleManager {

	public static volatile boolean updated = false;
	public static volatile boolean changed = false;
	public static volatile boolean cached = false;
	public static volatile boolean viewing = false;
	public static volatile boolean isRunning = false;
	public static volatile boolean isCacheing = false;

	public static volatile boolean refreshing = false;
	public static volatile int processes = 0;
	public static volatile boolean toClear = false; 

	private static volatile ArrayList<Article> articles = new ArrayList<Article>();
	private static volatile ArrayList<Article> articleBuffer = new ArrayList<Article>();
	private static volatile ArrayList<PullPair> nidsToPull = new ArrayList<PullPair>();

	private static Handler handler = new Handler();
	private static volatile ArrayList<ArticleChangeListener> listeners = new ArrayList<ArticleChangeListener>();

	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer, String> storyMap = new HashMap<Integer, String>();

	public static void safelyAddNid(ArrayList<Article> articles, Article a) {
		if(a != null) {
			int index = 0;
			while(index < articles.size()) {
				if(articles.get(index).nid == a.nid){
					articles.remove(index);
				} else {
					index++;
				}
			}
			articles.add(a);
		}
	}

	public static void addListener(ArticleChangeListener a) {
		if(!listeners.contains(a)) {
			listeners.add(a);
		}
	}

	public static void removeListener(ArticleChangeListener a) {
		if(listeners.contains(a)) {
			listeners.remove(a);
		}
	}

	public static void initialize(final Context context) {
		if(!isRunning) {
			isRunning = true;
			articles.addAll(getArticlesFromCache(context));
			for(ArticleChangeListener a : listeners) {
				a.onArticleUpdate();
			}
			(new Thread() {
				public void run() {
					while(isRunning) {
						for(ArticleChangeListener a : listeners) {
							if(nidsToPull.size() > 0 || articleBuffer.size() > 0)
								a.onLoadStart();
							else
								a.onLoadDone();
						}
						if(articleBuffer.size() > 0) {
							final ArrayList<Article> copy = new ArrayList<Article>();
							synchronized(articles) {
								copy.addAll(articles);
							}
							while(articleBuffer.size() > 0) {
								safelyAddNid(copy, articleBuffer.remove(0));
							}
							Collections.sort(copy);
							handler.post(new Runnable() {
								public void run() {
									articles = copy;
									for(ArticleChangeListener a : listeners) {
										a.onArticleUpdate();
									}
								}
							});
						}
						final ArrayList<PullPair> buffer = new ArrayList<PullPair>();
						synchronized(nidsToPull) {
							int size = Math.min(3, nidsToPull.size());
							for(int i = 0; i < size; i++) {
								buffer.add(nidsToPull.remove(0));
							}
						}
						if(buffer.size() > 0) {
							new Thread() {
								public void run() {
									for(PullPair pair : buffer) {
										if(!containsArticleWithNid(pair.nid)){ 
											final Article article = WebServer.getArticleWithNid(pair.nid);
											if(article != null) {
												article.storyType = pair.storyType;
												String story = WebServer.parseHTML(article.story);
												String subtitle = WebServer.parseHTML(article.imageSubtitle);
												article.story = story;
												article.imageSubtitle = subtitle;
												article.parsed = true;
												articleBuffer.add(article);
											}
										} else {
											Article focus = getArticleWithNid(pair.nid);
											focus.storyType += pair.storyType;
											articleBuffer.add(focus);
										}
									}
								}
							}.start();
						}
						try {
							Thread.sleep(1000);
						} catch(Exception e) {}	
					}
				}
			}).start();
		}
	}

	public  static ArrayList<Article> getArticlesOfType(String type) {
		ArrayList<Article> subArticles = new ArrayList<Article>();
		for(Article focus : articles) {
			String multitype = storyMap.get(focus.nid);
			if(multitype != null && multitype.contains(type))
				subArticles.add(focus);
		}
		return subArticles;
	}

	public static void clearArticlesOfType(String type) {
		articles.removeAll(getArticlesOfType(type));
	}

	public static ArrayList<Article> getArticlesFromCache(Context context) {
		while(isCacheing) {
			Log.d("SHS Falcon: ArticleManager", "Waiting for Cache to finish"); 
		}
		ArticleDataSource source = new ArticleDataSource(context);
		source.open();
		ArrayList<Article> toReturn = new ArrayList<Article>();
		try {
			toReturn.addAll(source.getCache());
		} catch(Exception e) {
			Toast.makeText(context, "Could not load cache", Toast.LENGTH_LONG).show();
		}
		source.close();
		return toReturn;
	}

	public static void cacheArticles(final Context context) {
		if(!isCacheing) {
			SectionManager.cache(context);
			Log.d("SHS Falcon: ArticleManager", "Starting cache");
			isCacheing = true;
			final ArrayList<Article> bucket = new ArrayList<Article>();
			(new Thread() {
				public void run() {
					for(int i = 0; i < Article.storyTypes.length; i++) {
						String focus = Article.storyTypes[i];
						int[] nids = SectionManager.getNids(focus);
						if(nids != null) {
							if(nids.length > 10) {
								nids = Arrays.copyOfRange(nids, 0, 10);
							}
							ArrayList<Article> temp = getArticlesWithNids(nids);
							for(Article a : temp) {
								safelyAddNid(bucket, a);
							}
						}
						Log.d("SHS Falcon: ArticleManager", "Cacheing articles");
					}
					ArticleDataSource source = new ArticleDataSource(context);
					source.open();
					source.clearCache();
					for(Article focus : bucket) {
						source.addArticle(focus);
					}
					Log.d("SHS Falcon: ArticleManager", bucket.size() + " articles cached");
					source.close();
					isCacheing = false;
				}
			}).start();
		}	
	}

	public static void pullArticleWithNid(final Context context, final int nid, final String storyType) {
		new Thread() {
			public void run() {
				if(!containsArticleWithNid(nid)) {
					PullPair toPull = new PullPair();
					toPull.nid = nid;
					storyMap.put(nid, storyType);
					toPull.storyType = storyType;
					synchronized(nidsToPull) {
						nidsToPull.add(toPull);
						//					}
					}
				}
			}
		}.start();
	}

	public static Article getArticleWithNid(final int nid) {
		int index = indexOfArticleWithNid(nid);
		if(index != -1) {
			return articles.get(index);
		} else {
			return null;
		}
	}

	public static ArrayList<Article> getArticlesWithNids(final int[] nids) {
		ArrayList<Article> subArticles = new ArrayList<Article>();
		if(nids != null) {
			for(int nid : nids) {
				Article article = getArticleWithNid(nid);
				if(article != null) 
					subArticles.add(article);
			}
		}
		return subArticles;
	}

	public static int indexOfArticleWithNid(int nid) {
		for(int i = 0; i < articles.size(); i++) {
			if(articles.get(i).nid == nid) 
				return i;
		}
		return -1;
	}

	public static boolean containsArticleWithNid(int nid) {
		synchronized(articles) {
			for(int i = 0; i < articles.size(); i++) {
				if(articles.get(i).nid == nid && !articles.get(i).cached) 
					return true;
			}
			return false;
		}
	}

	public static void clearArticleCache() {
		handler.post(new Runnable() {
			public void run() {
				int index = 0;
				while(index < articles.size()) {
					if(articles.get(index).cached)
						articles.remove(index);
					else
						index++;
				}
				changed = true;
			}
		});
	}

	public  static void clearCache(Context context) {
		ArticleDataSource source = new ArticleDataSource(context);
		source.open();
		source.clearCache();
		source.close();
	}

	public static class PullPair {
		int nid;
		String storyType;
	}

}
