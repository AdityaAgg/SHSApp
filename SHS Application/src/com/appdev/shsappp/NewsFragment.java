package com.appdev.shsappp;

import android.view.LayoutInflater;
import android.view.View;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;

public class NewsFragment extends BaseFragment {
	
	public String getType() {
		return "news";
	}

	public String getName() {
		return "News";
	}
	
	public int getImage() {
		return R.drawable.news;
	}
	
	@Override
	public View getInflater(LayoutInflater inflater, Article article) {
		View item = ArticleInflater.inflateRegularItem(inflater, article);
		return item;
	}

}
