package com.appdev.shsappp;

import android.view.LayoutInflater;
import android.view.View;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;

public class FeaturesFragment extends BaseFragment {

	public String getType() {
		return "features";
	}
	
	public String getName() {
		return "Features";
	}
	
	public int getImage() {
		return R.drawable.features;
	}

	@Override
	public View getInflater(LayoutInflater inflater, Article article) {
		View item = ArticleInflater.inflateRegularItem(inflater, article);
		return item;
	}

}
