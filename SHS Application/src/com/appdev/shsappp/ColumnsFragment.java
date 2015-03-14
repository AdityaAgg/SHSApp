package com.appdev.shsappp;

import android.view.LayoutInflater;
import android.view.View;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;

public class ColumnsFragment extends BaseFragment {

	public String getType() {
		return "columns";
	}
	
	public String getName() {
		return "Columns";
	}
	
	public int getImage() {
		return R.drawable.columns;
	}

	@Override
	public View getInflater(LayoutInflater inflater, Article article) {
		View item = ArticleInflater.inflateRegularItem(inflater, article);
		return item;
	}
}
