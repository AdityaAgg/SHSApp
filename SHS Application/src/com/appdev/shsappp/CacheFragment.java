package com.appdev.shsappp;

import java.util.ArrayList;

import android.support.v4.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;
import com.appdev.shsappp.article.ArticleManager;
import com.appdev.shsappp.webserver.WebServer;

public class CacheFragment extends ListFragment{

	int[] nids = new int[] {};

	volatile Handler handler;
	volatile BaseAdapter adapter;
	volatile boolean updated = true;
	volatile boolean isUpdating = false;
	volatile boolean isCached = false;
	public volatile boolean isFetching = true;

	ArrayList<Article> articles = new ArrayList<Article>();

	public CacheFragment() {
		handler = new Handler();
	}

	public View getInflater(LayoutInflater inflater, Article article) {
		View item = ArticleInflater.inflateRegularItem(inflater, article);
		return item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		articles = ArticleManager.getArticlesFromCache(getActivity());
		adapter = new DefaultAdapter();
		setListAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		((NewsActivity)getActivity()).viewArticle(articles.get(position));
	}

	@Override
	public void onResume() {
		super.onResume();
		this.getListView().setDivider(null);
		this.notifyChange();
	}



	@Override
	public void onPause() {
		super.onPause();
		WebServer.close();
		isUpdating = false;
	}

	public void notifyChange() {
		handler.post(new Runnable() {
			public void run() {
				if(adapter != null) {
					adapter.notifyDataSetChanged();				
				}
			}
		});
	}

	public class DefaultAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = new View(getActivity());
			item = getInflater(
					getActivity().getLayoutInflater(), articles.get(position));
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
			return articles.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			if(articles.size() == 0)
				return -1;
			if(position < articles.size()) {
				return articles.get(position).nid;
			}
			return 0;
		}
	}
}