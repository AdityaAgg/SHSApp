package com.appdev.shsappp;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends ListActivity {
	private DirectoryDataSource datasource;
	private List<Contact> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasource = new DirectoryDataSource(this);
		datasource.open();
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			list = getSearchResults(query);
			ArrayAdapter<Contact> contacts = new ArrayAdapter<Contact>(this, 
					android.R.layout.simple_list_item_1, list);	
			this.setListAdapter(contacts);
			this.setTitle("Results for \"" + query + "\"");
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ContactActivity.class);
		intent.putExtra("toDisplay", list.get(position));
		startActivity(intent);
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
		return true;
	}

	public List<Contact> getSearchResults(String query) {
		List<Contact> all = datasource.getAllContacts();
		List<Contact> result = new ArrayList<Contact>();
		System.out.println(query);
		for(Contact contact : all) {
			StringTokenizer tags = new StringTokenizer(query);
			boolean match = true;
			while(tags.hasMoreTokens()) {
				String token = tags.nextToken();
				if(!contact.name.toLowerCase().contains(token.toLowerCase())) {
					match = false;
				}
			}
			if(match) result.add(contact);
		}
		return result;
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}
