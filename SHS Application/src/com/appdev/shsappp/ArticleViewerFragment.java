package com.appdev.shsappp;

import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleInflater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArticleViewerFragment extends Fragment {
        
        private Article article;
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setRetainInstance(true);
        }
        
        @Override
        public void onResume() {
                super.onResume();
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
                article = (Article)getArguments().getSerializable("article");
                return ArticleInflater.inflateArticleFragment(inflater, article);
        }
}