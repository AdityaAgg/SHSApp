package com.appdev.shsappp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.appdev.shsappp.article.Article;

public class ArticleViewerActivity extends SherlockFragmentActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Article article = (Article)getIntent().getExtras().getSerializable("article");
		setContentView(R.layout.activity_fragment_holder);
		Bundle args = new Bundle();
        args.putSerializable("article", article);
        Fragment fragment = new ArticleViewerFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.main, fragment)
        .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if (item.getItemId()==android.R.id.home) {
				Intent mainer=new Intent(this, NewsActivity.class);
				startActivity(mainer);
		}else if(item.getItemId()==R.id.about_main){
			Intent about=new Intent(this, AboutUsActivity.class);
			startActivity(about);
		}else if(item.getItemId()==R.id.student_id_main){
			Intent st_id=new Intent(this, StudentID.class);
			startActivity(st_id);
		}else{
			return super.onOptionsItemSelected(item);
		}
		return true;
	}


	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		menu.clear();
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
				return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
}
