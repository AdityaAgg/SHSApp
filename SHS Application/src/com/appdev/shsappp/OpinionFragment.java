package com.appdev.shsappp;

import android.view.LayoutInflater;
import android.view.View;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;

public class OpinionFragment extends BaseFragment {

	public String getType() {
		return "opinion";
	}
	
	public String getName() {
		return "Opinion";
	}
	
	public int getImage() {
		return R.drawable.opinion;
	}

	@Override
	public View getInflater(LayoutInflater inflater, Article article) {
		View item = ArticleInflater.inflateRegularItem(inflater, article);
		return item;
	}
}
