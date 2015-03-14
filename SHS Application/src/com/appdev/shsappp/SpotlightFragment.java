package com.appdev.shsappp;

import android.view.LayoutInflater;
import android.view.View;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;

public class SpotlightFragment extends BaseFragment {

	public String getType() {
		return "spotlight";
	}
	
	public String getName() {
		return "Spotlight";
	}
	
	public int getImage() {
		return R.drawable.spotlight;
	}

	@Override
	public View getInflater(LayoutInflater inflater, Article article) {
		View item = ArticleInflater.inflateSpotlightItem(inflater, article);
		return item;
	}

}
