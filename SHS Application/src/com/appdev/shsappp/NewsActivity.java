package com.appdev.shsappp;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.appdev.shsappp.article.Article;
import com.appdev.shsappp.article.ArticleChangeListener;
import com.appdev.shsappp.article.ArticleManager;
import com.appdev.shsappp.article.SectionManager;

public class NewsActivity extends SherlockFragmentActivity implements ArticleChangeListener, OnNavigationListener {

	private Handler handler = new Handler();
	private String[] subcategories;

	private ViewPager viewPager;
	private FragmentPagerAdapter adapter;
	private MenuItem refresh;

	private boolean load;

	private HashMap<String, Fragment> map = new HashMap<String, Fragment>();

	private static String getFragmentTag(int index) {
		return "android:switcher:" + R.id.pager + ":" + index;
	}

	public void viewArticle(Article article) {
		Intent intent = new Intent(this, ArticleViewerActivity.class);
		intent.putExtra("article", article);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		subcategories = getResources().getStringArray(R.array.array_activity_main_subcategories);
		viewPager = (ViewPager) findViewById(R.id.pager);
		adapter = new SectionsPagerAdapter(getSupportFragmentManager());
		SectionManager.initialize(this);

		map.put("Spotlight", new SpotlightFragment());
		map.put("News", new NewsFragment());
		map.put("Sports", new SportsFragment());
		map.put("Opinion", new OpinionFragment());
		map.put("Columns", new ColumnsFragment());
		map.put("Features", new FeaturesFragment());
		map.put("Cache", new CacheFragment());

		viewPager.setAdapter(adapter);
	}

	public void onArticleUpdate() {
		for(int i = 0; i < subcategories.length; i++) {
			Fragment focus = getSupportFragmentManager().findFragmentByTag(getFragmentTag(i));
			if(focus instanceof BaseFragment) {
				((BaseFragment)focus).notifyArticleChange();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		ArticleManager.initialize(this);
		ArticleManager.addListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		ArticleManager.cacheArticles(this);
		ArticleManager.removeListener(this);
	}

	public void setLoad(final boolean loading) {
		handler.post(new Runnable() {
			public void run() {
				if(refresh != null) {
					if(load != loading) {
						load = loading;
						if(load) {
							refresh.setActionView(R.layout.actionbar_compat);
						} else {
							refresh.setActionView(null);
						}
					}
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment main = getSupportFragmentManager().findFragmentByTag(
				getFragmentTag(viewPager.getCurrentItem()));
	
		if (item.getItemId()==android.R.id.home) {
				Intent mainer=new Intent(this, MainActivity.class);
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

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			fragment = map.get(subcategories[i]);
			if(fragment == null) {
				fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;	
			} else {
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return subcategories.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return subcategories[position];
		}
	}

	public static class DummySectionFragment extends Fragment {
		public DummySectionFragment() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
			return textView;
		}
	}

	@Override
	public void onLoadStart() {
		setLoad(true);

	}

	@Override
	public void onLoadDone() {
		setLoad(false);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		viewPager.setCurrentItem(itemPosition, true);
		return true;
	}

}
