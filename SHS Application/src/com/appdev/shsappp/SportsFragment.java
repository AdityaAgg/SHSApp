package com.appdev.shsappp;

import android.view.LayoutInflater;
import android.view.View;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;

public class SportsFragment extends BaseFragment {

	public String getType() {
		return "sports";
	}
	
	public String getName() {
		return "Sports";
	}
	
	public int getImage() {
		return R.drawable.sports;
	}

	@Override
	public View getInflater(LayoutInflater inflater, Article article) {
		View item = ArticleInflater.inflateRegularItem(inflater, article);
		return item;
	}
}
