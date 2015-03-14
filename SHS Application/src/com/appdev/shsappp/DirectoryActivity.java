package com.appdev.shsappp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DirectoryActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DirectoryDataSource datasource = new DirectoryDataSource(this);
		datasource.open();
		ArrayAdapter<String> subdirectories = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, 
				Contact.categories
		);
		
		this.setListAdapter(subdirectories);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.search_main){
			onSearchRequested();
			return true;
		}
		else if(item.getItemId()==R.id.about_main){
			Intent intent=new Intent(this, AboutUsActivity.class);
			startActivity(intent);
			return true;
		}
			else if(item.getItemId()==R.id.student_id_main){
			Intent intent_student=new Intent(this, StudentID.class);
			startActivity(intent_student);
			return true;
			} else if(item.getItemId()==android.R.id.home){
				Intent intent_home=new Intent(this, MainActivity.class);
				startActivity(intent_home);
				return true;
			}
			else{
			return super.onOptionsItemSelected(item);
			}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, SubdirectoryActivity.class);
		intent.putExtra("filter", Contact.categories[position]);
		startActivity(intent);
	}
}
