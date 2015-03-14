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

public class SavedActivity extends ListActivity {
	private DirectoryDataSource datasource;
	private List<Contact> list;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ContactActivity.class);
		intent.putExtra("toDisplay", list.get(position));
		startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new DirectoryDataSource(this);
		datasource.open();
		ArrayAdapter<Contact> contacts = null;
		
		list = datasource.getFilterSavedContacts();
		contacts = new ArrayAdapter<Contact>(this, 
				android.R.layout.simple_list_item_1, list);	
		this.setTitle(this.getString(R.string.title_activity_saved));
		this.setListAdapter(contacts);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			return true;
		} else if (itemId == R.id.search) {
			onSearchRequested();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	protected void onResume() {
		datasource.open();
		ArrayAdapter<Contact> contacts = null;
		
		list = datasource.getFilterSavedContacts();
		contacts = new ArrayAdapter<Contact>(this, 
				android.R.layout.simple_list_item_1, list);	
		this.setTitle(this.getString(R.string.title_activity_saved));
		this.setListAdapter(contacts);
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}
