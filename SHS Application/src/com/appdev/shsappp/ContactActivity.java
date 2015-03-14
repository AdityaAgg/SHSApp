package com.appdev.shsappp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Intents;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

public class ContactActivity extends Activity {
	private DirectoryDataSource datasource;
	private Contact toDisplay;
	private CheckBox saved;
	private Button phone, email, website;
	public ArrayList<GridItemContact> gridItemer;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_layout);
		
		
		final Context context = this;
		
		
		
		datasource = new DirectoryDataSource(this);
		datasource.open();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		toDisplay = (Contact)getIntent().getSerializableExtra("toDisplay");
		getActionBar().setTitle(toDisplay.name);
		TextView teachertitle=(TextView)findViewById(R.id.contact_name);
		teachertitle.setText(toDisplay.name);
		
		
		
		
		gridItemer = new ArrayList<GridItemContact>();
		gridItemer.add(new GridItemContact("Email", getResources().getDrawable(R.drawable.mail)));
		gridItemer.add(new GridItemContact("Go to Website", getResources().getDrawable(R.drawable.web)));
		gridItemer.add(new GridItemContact("Save to Contacts", getResources().getDrawable(R.drawable.save)));
		gridItemer.add(new GridItemContact("Call", getResources().getDrawable(R.drawable.phone)));
		GridView gridMenu = (GridView)findViewById(R.id.grid_menu_contact);
		gridMenu.setVerticalFadingEdgeEnabled(true);
		gridMenu.setPadding(0, 0, 0, 0);
		
		gridMenu.setAdapter(new GridMenuAdapter());
	    gridMenu.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            if (position==0){
	            	Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/html");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[]{toDisplay.email});
					startActivity(intent);
	            }
	            else if(position==1){
	            	Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(toDisplay.website));
					startActivity(intent);
	            }else if(position==2){
	            	Intent intent = new Intent(Intents.Insert.ACTION);
	            	intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
	            	intent.putExtra(Intents.Insert.EMAIL, toDisplay.email)
	            	.putExtra(Intents.Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_WORK)
	            	.putExtra(Intents.Insert.PHONE, "408-867-3411-" + toDisplay.extension.substring(1))
	            	.putExtra(Intents.Insert.PHONE_TYPE, Phone.TYPE_WORK)
	            	.putExtra(Intents.Insert.NAME, toDisplay.name);
	            	startActivity(intent);
	            	
	            } else if(position==3){
	            	new AlertDialog.Builder(context)
					.setTitle("Calling Contact")
					.setMessage("Are you sure you want to call?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String uri = "tel:408-867-3411-" + toDisplay.extension.substring(1) ;
							Intent intent = new Intent(Intent.ACTION_CALL);
							intent.setData(Uri.parse(uri));
							startActivity(intent);
						}

					})
					.setNegativeButton("No", null)
					.show();
	            	
	            }
	        }
	    });
		
		
		
		
		
		
		
		
		
		
		/*
		saved = (CheckBox)findViewById(R.id.contact_saved);
		saved.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				datasource.setSaved(toDisplay.id, isChecked);
				toDisplay.saved = isChecked;
			}

		});*/
		//phone = (Button)findViewById(R.id.contact_phone);
		//email = (Button)findViewById(gr);
		/*email.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[]{toDisplay.email});
				startActivity(intent);

			}
		});*/
		
		
		//setTitle(toDisplay.name);
		/*
		saved.setChecked(toDisplay.saved);*/
		/*if(toDisplay.extension.equals("none")) {
			phone.setEnabled(false);
		} else {
			phone.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(context)
					.setTitle("Calling Contact")
					.setMessage("Are you sure you want to call?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String uri = "tel:408-867-3411-" + toDisplay.extension.substring(1) ;
							Intent intent = new Intent(Intent.ACTION_CALL);
							intent.setData(Uri.parse(uri));
							startActivity(intent);
						}

					})
					.setNegativeButton("No", null)
					.show();
				}
			});
		}
		email.setText(toDisplay.email);
		website.setText(toDisplay.website);
		website.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(toDisplay.website));
				startActivity(intent);
			}
		});*/
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
				intent_home.putExtra("filter", getIntent().getStringExtra("filter"));
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
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
	public class GridMenuAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = getLayoutInflater().inflate(R.layout.grid_item_layout, null);
			item.setPadding(0, 50, 0, 40);
			GridItemContact current = gridItemer.get(position);
			TextView label = (TextView)item.findViewById(R.id.item_label);
			label.setText(current.text);
			label.setCompoundDrawables(null, current.image, null, null);
			label.setCompoundDrawablePadding(32);
			item.setMinimumHeight(300);
			return item;
		}

		@Override
		public final int getCount() {
			return gridItemer.size();
		}

		@Override
		public Object getItem(int position) {
			return gridItemer.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	public static class GridItemContact {
		public String text;
		public Drawable image;
		public GridItemContact(String text, Drawable image) {
			this.text = text;
			this.image = image;
			this.image.setBounds(0, 0, 160, 160);
		}
	}

	
}
