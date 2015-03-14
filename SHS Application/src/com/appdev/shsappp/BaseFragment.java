package com.appdev.shsappp;
import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleManager;
import com.appdev.shsappp.article.SectionManager;
import com.appdev.shsappp.webserver.WebServer;

public abstract class BaseFragment extends ListFragment {

	int[] nids = new int[] {};

	volatile Handler handler;
	volatile BaseAdapter adapter;
	volatile boolean isUpdating = false;
	volatile boolean isCached = false;
	public volatile boolean isFetching = true;
	public volatile boolean displayLoadMore = true;

	ArrayList<Article> articles = new ArrayList<Article>();

	public BaseFragment() {
		handler = new Handler();
		int[] temp = SectionManager.getNids(getType());
		if(temp == null) {
			new Thread() {
				public void run() {
					int[] superTemp = null;
					while(superTemp == null) {
						superTemp = SectionManager.getNids(getType());
					}
					if(pullCache()) {
						if(superTemp != null)
							nids = superTemp;
						notifyNidChange();
					}
				}
			}.start();
		} else {
			if(pullCache()) {
				if(temp != null) {
					nids = temp;
				}
			}
		}
	}

	public abstract View getInflater(LayoutInflater inflater, Article article);
	public abstract String getType();
	public abstract String getName();
	public abstract int getImage();

	public boolean pullCache() {
		return true;
	}

	public void pushForMore(boolean append) {
		isFetching = true;
		//Log.d("SHS Falcon: Fragment", "Pulling nids...");
		if(append) {
			(new Thread() {
				public void run() {
					int toPush = ArticleManager.getArticlesWithNids(nids).size() + 10;
					int[] newNids = WebServer.getStoryNids(getType(), toPush);
					if(newNids != null) {
						nids = newNids;
						notifyNidChange();
						//Log.d("SHS Falcon: Fragment", "Nids Changed...");
					} else {
						int[] temp = SectionManager.getNids(getType());
						if(pullCache()) {
							if(temp != null) {
								nids = temp;
							}
						}
					}
					isFetching = false;
				}
			}).start(); 
		} else {
			(new Thread() {
				public void run() {
					nids = new int[0];
					int toPush = 10;
					int[] newNids = WebServer.getStoryNids(getType(), toPush);
					if(newNids != null) {
						nids = newNids;
						notifyNidChange();
						Log.d("SHS Falcon: Fragment", "Nids Changed...");
					} else {
						int[] temp = SectionManager.getNids(getType());
						if(pullCache()) {
							if(temp != null) {
								nids = temp;
							}
						}
					}
					isFetching = false;
				}
			}).start();
		}
	}

	public void notifyNidChange() {
		if(pullCache()) {
			SectionManager.update(getType(), nids);
		}
		refreshArticles();
	}

	public void notifyAdapterChange() {
		handler.post(new Runnable() {
			public void run() {
				if(adapter != null) {
					adapter.notifyDataSetChanged();				
				}
			}
		});
	}

	public void notifyArticleChange() {
		new Thread() {
			public void run() {
				final ArrayList<Article> temp = ArticleManager.getArticlesWithNids(nids);
				replaceList(temp);
			}
		}.start();
	}

	public void replaceList(final ArrayList<Article> temp) {
		handler.post(new Runnable() {
			public void run() {
				articles = temp;
				notifyAdapterChange();
			}
		});
	}

	public void refreshArticles() {
		for(int i = 0; i < nids.length; i++) {
			ArticleManager.pullArticleWithNid(getActivity(), nids[i], getName());
		}
		notifyArticleChange();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		adapter = new DefaultAdapter();
		setListAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(id == 0) {
			pushForMore(true);
		} else {
			((NewsActivity)getActivity()).viewArticle(ArticleManager.getArticleWithNid((int)id));
		}
	}

	@Override
	public void onResume() {
		notifyArticleChange();
		if(articles.size() == 0) {
			pushForMore(false);
		}
		super.onResume();
		this.getListView().setDivider(null);
		this.notifyAdapterChange();
		WebServer.close();
		WebServer.initialize();
		isUpdating = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		WebServer.close();
		isUpdating = false;
	}

	public class DefaultAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = new View(getActivity());
			if(position < articles.size()) {
				item = getInflater(
						getActivity().getLayoutInflater(), articles.get(position));
			} else if(!isCached) {
				item = getActivity().getLayoutInflater().inflate(R.layout.item_load_more, null);
			}
			Resources r = getResources();
			float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());
			int padding = (int)px;
			int top = (position == 0) ? 2*padding : padding;
			int bottom = (position == getCount() - 1) ? 2*padding : padding;
			item.setPadding(2*padding, top, 2*padding, bottom);
			return item;
		}

		@Override
		public final int getCount() {
			if(articles.size() == 0)
				return 0;
			if(displayLoadMore) {
				return articles.size() + 1;
			} else {
				return articles.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			if(position < articles.size()) {
				return articles.get(position).nid;
			}
			return 0;
		}
	}
}
