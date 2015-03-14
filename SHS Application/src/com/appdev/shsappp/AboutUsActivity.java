package com.appdev.shsappp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.appdev.shsappp.ContactActivity.GridItemContact;

public class AboutUsActivity extends Activity {

	Button seeCode;
	Button tips;
	public ArrayList<GridItemContact> gridItemerAbout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_layout);
		gridItemerAbout = new ArrayList<GridItemContact>();
		gridItemerAbout.add(new GridItemContact("Email", getResources().getDrawable(R.drawable.email)));
		gridItemerAbout.add(new GridItemContact("Twitter Page", getResources().getDrawable(R.drawable.twitter)));
		gridItemerAbout.add(new GridItemContact("Facebook Page", getResources().getDrawable(R.drawable.facebook)));
		gridItemerAbout.add(new GridItemContact("Go to Website", getResources().getDrawable(R.drawable.aboutinternet)));
		GridView gridMenu = (GridView)findViewById(R.id.grid_about);
		gridMenu.setVerticalFadingEdgeEnabled(true);
		gridMenu.setPadding(0, 0, 0, 0);
		
		gridMenu.setAdapter(new GridMenuAdapter());
	    gridMenu.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Log.d("Position","Position: "+position);
	            if (position==0){
	            	Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/html");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "shsappdev@gmail.com" });
					startActivity(intent);
	            }
	            else if(position==1){
	            	Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://twitter.com/SHSAppDev"));
					startActivity(intent);
	            }else if(position==2){
	            	Intent intent = new Intent(Intent.ACTION_VIEW,
	    					Uri.parse("https://www.facebook.com/SHSAppDev"));
	    					startActivity(intent);
	            	
	            } else if(position==3){
	            	Intent intent = new Intent(Intent.ACTION_VIEW,
	    					Uri.parse("http://appdevclubshs.com"));
	    					startActivity(intent);	
	            }
	        }
	    });
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gen_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id==android.R.id.home){
			Intent main=new Intent(this, MainActivity.class);
			startActivity(main);
		} else if(item.getItemId()==R.id.about_main){
			Intent about=new Intent(this, AboutUsActivity.class);
			startActivity(about);
			return true;
		}else if(item.getItemId()==R.id.student_id_main){
			Intent st_id=new Intent(this, StudentID.class);
			startActivity(st_id);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public class GridMenuAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = getLayoutInflater().inflate(R.layout.grid_item_layout,
					null);
			item.setPadding(0, 50, 0, 40);
			GridItemContact current = gridItemerAbout.get(position);
			TextView label = (TextView) item.findViewById(R.id.item_label);
			label.setText(current.text);
			label.setCompoundDrawables(null, current.image, null, null);
			label.setCompoundDrawablePadding(32);
			item.setMinimumHeight(300);
			return item;
		}

		@Override
		public final int getCount() {
			return gridItemerAbout.size();
		}

		@Override
		public Object getItem(int position) {
			return gridItemerAbout.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	public static class GridItemAbout {
		public String text;
		public Drawable image;

		public GridItemAbout(String text, Drawable image) {
			this.text = text;
			this.image = image;
			this.image.setBounds(0, 0, 160, 160);
		}
	}
}
