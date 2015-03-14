package com.appdev.shsappp;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {
	public ArrayList<GridItem> gridItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		DirectoryDataSource datasource = new DirectoryDataSource(this);
		datasource.open();
		if(datasource.getAllContacts().size()==0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("The first time you use the app, you must connect to the internet to download the directory, schedules, and news articles.").setTitle("Connect to the Internet and Restart App for all Features")
			       .setCancelable(true).setPositiveButton("Yeah, I got it.", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                //
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
		gridItems = new ArrayList<GridItem>();
		gridItems.add(new GridItem("Schedule", getResources().getDrawable(R.drawable.schedule),
				new Intent(this, ScheduleActivity.class)));
		Intent aeries = new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://aeries.lgsuhsd.org/aeries.net/loginparent.aspx"));
		gridItems.add(new GridItem("Aeries", getResources().getDrawable(R.drawable.aeries),
				aeries));
		Intent canvas = new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://lgsuhsd.instructure.com/login"));
		gridItems.add(new GridItem("Canvas", getResources().getDrawable(R.drawable.canvas),
				canvas));
		gridItems.add(new GridItem("Falcon Newspaper", getResources().getDrawable(R.drawable.falconpaper),
				new Intent(this, NewsActivity.class)));
		gridItems.add(new GridItem("Directory", getResources().getDrawable(R.drawable.contacts),
				new Intent(this, DirectoryActivity.class)));
		Intent naviance = new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://connection.naviance.com/family-connection/auth/login/?hsid=saratogahigh"));
		gridItems.add(new GridItem("Naviance", getResources().getDrawable(R.drawable.naviance),
				naviance));
		gridItems.add(new GridItem("Announcements", getResources().getDrawable(R.drawable.annoucement),
				new Intent(this, Annoucements.class)));
		gridItems.add(new GridItem("Sports Center", getResources().getDrawable(R.drawable.sportscenter),
				new Intent(this, SportsCenterActivity.class)));

		GridView gridMenu = (GridView)findViewById(R.id.grid_menu);
		gridMenu.setVerticalFadingEdgeEnabled(true);
		gridMenu.setPadding(0, 0, 0, 0);
		gridMenu.setOnItemClickListener(this);
		gridMenu.setAdapter(new GridMenuAdapter());
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
		if(item.getItemId()==R.id.about_main){
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
			View item = getLayoutInflater().inflate(R.layout.grid_item_layout, null);
			item.setPadding(0, 50, 0, 40);
			GridItem current = gridItems.get(position);
			TextView label = (TextView)item.findViewById(R.id.item_label);
			label.setText(current.text);
			label.setCompoundDrawables(null, current.image, null, null);
			label.setCompoundDrawablePadding(32);
			item.setMinimumHeight(300);
			return item;
		}

		@Override
		public final int getCount() {
			return gridItems.size();
		}

		@Override
		public Object getItem(int position) {
			return gridItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	public static class GridItem {
		public String text;
		public Drawable image;
		public Intent intent;
		public GridItem(String text, Drawable image, Intent intent) {
			this.text = text;
			this.image = image;
			this.image.setBounds(0, 0, 160, 160);
			this.intent = intent;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		GridItem item = gridItems.get(position);
		startActivity(item.intent);
	}
}
