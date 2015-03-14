package com.appdev.shsappp.article;
import java.util.Calendar;

import android.content.res.Resources;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev.shsappp.R;

public class ArticleInflater {

	public static View inflateArticleFragment(LayoutInflater inflater, Article article) {
		View item = inflater.inflate(R.layout.fragment_article_viewer, null);
		((TextView)item.findViewById(R.id.title)).setText(Html.fromHtml(article.title));
		((TextView)item.findViewById(R.id.author)).setText(Html.fromHtml(article.author));
		((TextView)item.findViewById(R.id.date)).setText(article.date);
		((TextView)item.findViewById(R.id.story)).setText(Html.fromHtml(article.story));
		((TextView)item.findViewById(R.id.copyright)).setText("Copyright \u00a9 1959 - " + Calendar.getInstance().get(Calendar.YEAR));
		if(article.hasImage() && article.image != null) {
			if(!article.imageSubtitle.equals("")) {
				((TextView)item.findViewById(R.id.subtitle)).setText(Html.fromHtml(article.imageSubtitle));
			} else {
				((TextView)item.findViewById(R.id.subtitle)).getLayoutParams().height = 0;
			}
			((ImageView)item.findViewById(R.id.image)).setImageBitmap(article.image.bitmap);
		} else {
			((TextView)item.findViewById(R.id.subtitle)).getLayoutParams().height = 0;
			((ImageView)item.findViewById(R.id.image)).getLayoutParams().height = 0;
		}
		return item;
	}

	public static View inflateSpotlightItem(LayoutInflater inflater, Article article) {
		View item = inflater.inflate(R.layout.item_spotlight, null);
		((TextView)item.findViewById(R.id.title)).setText(Html.fromHtml(article.title));
		((TextView)item.findViewById(R.id.author)).setText(Html.fromHtml(article.author));
		if(article.hasImage() && article.image != null) {
			if(!article.imageSubtitle.equals("") && article.parsed) {
				((TextView)item.findViewById(R.id.subtitle)).setText(Html.fromHtml(article.imageSubtitle));
			} else {
				((TextView)item.findViewById(R.id.subtitle)).getLayoutParams().height = 0;
			}
			((ImageView)item.findViewById(R.id.image)).setImageBitmap(article.image.bitmap);
		} else {
			((TextView)item.findViewById(R.id.image_flap)).getLayoutParams().height = 0;
			((TextView)item.findViewById(R.id.subtitle)).getLayoutParams().height = 0;
			((ImageView)item.findViewById(R.id.image)).getLayoutParams().height = 0;
		}
		return item;
	}

	public static View inflateRegularItem(LayoutInflater inflater, Article article) {
		View item = inflater.inflate(R.layout.item_regular, null);
		((TextView)item.findViewById(R.id.title)).setText(Html.fromHtml(article.title));
		((TextView)item.findViewById(R.id.author)).setText(Html.fromHtml(article.author));
		if(article.hasImage() && article.image != null) {
			((ImageView)item.findViewById(R.id.image)).setImageBitmap(article.image.bitmap);
			Resources r = inflater.getContext().getResources();
			(item.findViewById(R.id.content)).getLayoutParams().height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105, r.getDisplayMetrics());
		} else {
			((ImageView)item.findViewById(R.id.image)).getLayoutParams().height = 0;
			((ImageView)item.findViewById(R.id.image)).getLayoutParams().width = 0;
		}
		return item;
	}

}
