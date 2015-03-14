package com.appdev.shsappp;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SubdirectoryActivity extends ListActivity {
	private DirectoryDataSource datasource;
	private List<Contact> list;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ContactActivity.class);
		intent.putExtra("toDisplay", list.get(position));
		String filter = getIntent().getStringExtra("filter");
		intent.putExtra("filter",filter);
		startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new DirectoryDataSource(this);
		datasource.open();
		ArrayAdapter<Contact> contacts = null;
		
		String filter = getIntent().getStringExtra("filter");
		
		if(filter.equals("All")) {
			list = datasource.getAllContacts();
		} else {
			list = datasource.getFilterRoleContacts(filter);
		}
		contacts = new ArrayAdapter<Contact>(this, 
				android.R.layout.simple_list_item_1, list);	
		this.setTitle(this.getString(R.string.title_activity_directory) + ": " + filter);
		this.setListAdapter(contacts);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
		datasource.open();
		ArrayAdapter<Contact> contacts = null;
		String filter = getIntent().getStringExtra("filter");
		
		if(filter.equals("All")) {
			list = datasource.getAllContacts();
		} else {
			list = datasource.getFilterRoleContacts(filter);
		}
		contacts = new ArrayAdapter<Contact>(this, 
				android.R.layout.simple_list_item_1, list);	
		this.setListAdapter(contacts);
		super.onResume();
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
				Intent intent_home=new Intent(this, DirectoryActivity.class);
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
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}
