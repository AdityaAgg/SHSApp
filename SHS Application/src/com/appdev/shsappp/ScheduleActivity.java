package com.appdev.shsappp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class ScheduleActivity extends Activity implements OnItemSelectedListener {
	private final long SLEEP_TIME = 5000;
	private ListView scheduleList;
	private Spinner spinner;
	private ArrayList<ArrayList<ScheduleToken>> schedules;
	public Handler handler;
	public Thread updateThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		spinner = (Spinner)findViewById(R.id.spinner_day);
		scheduleList = (ListView)findViewById(R.id.list_schedule);
		Calendar c = Calendar.getInstance(); 
		if(c.get(Calendar.DAY_OF_WEEK)==7 || c.get(Calendar.DAY_OF_WEEK)==1) {
			spinner.setSelection(0);
		}else {
		spinner.setSelection(c.get(Calendar.DAY_OF_WEEK)-2);
		}
		spinner.setOnItemSelectedListener(this);
		handler = new Handler();
		
			//Log.d("working","working");
			try {
				schedules = parseSchedules();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.d("Exception",e1.toString()+"hihihihi");
			}
			Log.d("sizeschedule","sizeschedule"+schedules.size());

		scheduleList.setAdapter(new ScheduleAdapter(schedules.get(spinner.getSelectedItemPosition())));
		updateThread = new Thread() {
			public void run() {
				while(!isFinishing()) {
					handler.post(updateList());
					try {
						Thread.sleep(SLEEP_TIME);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		updateThread.start();
	
	}

	@Override
	public void onResume() {
		super.onResume();
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
		}else if(item.getItemId()==R.id.about_main){
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

	public Runnable updateList() {
		return new Runnable() {
			public void run() {
				Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				ArrayList<ScheduleToken> schedule = schedules.get(spinner.getSelectedItemPosition());
				ScheduleAdapter sa = ((ScheduleAdapter)scheduleList.getAdapter());
				if(sa.areAllItemsEnabled()) {
					for(int i = 0; i < scheduleList.getCount(); i++) {
						int percent = schedule.get(i).percent(hour, minute);
						View v = (View)sa.getItem(i);
						if(v != null) {
							ProgressBar progress = (ProgressBar)v.findViewById(R.id.item_progress);
							if(percent >= 0) {
								progress.setVisibility(View.VISIBLE);
								progress.setProgress(percent);
							} else {
								progress.setVisibility(View.INVISIBLE);
							}
						}
					}
				}
			}
		};
	}

	public ArrayList<ArrayList<ScheduleToken>> parseSchedules() throws Exception {
		ArrayList<ArrayList<ScheduleToken>> schedules = new ArrayList<ArrayList<ScheduleToken>>();
		Log.d("helo","helo");
		DirectoryDataSource datasourcer = new DirectoryDataSource(this);
		datasourcer.open();
		String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday"};
		for(int i = 0; i < 5; i++) {
			List<Schedule> filteredDaySchedules=datasourcer.getFilterDaySchudules(days[i]);
			ArrayList<ScheduleToken> schedule = new ArrayList<ScheduleToken>();
			
			int n = filteredDaySchedules.size();
			for(int j = 0; j < n; j++) {
				
				String startTime = filteredDaySchedules.get(j).timestart;
				startTime=startTime.trim();
				Log.d("StartTime","StartTime"+startTime);
				String endTime = filteredDaySchedules.get(j).timefinish;
				endTime=endTime.trim();
				String name = filteredDaySchedules.get(j).periodname;
				int houra=Integer.parseInt((startTime.substring(0,startTime.indexOf(':'))));
				int hourb=Integer.parseInt((endTime.substring(0,endTime.indexOf(':'))));
				if(houra<7){
					houra=houra+12;
				}
				if(hourb<7){
					hourb=hourb+12;
				}
				ScheduleToken t = new ScheduleToken(
						houra, 
						Integer.parseInt((startTime.substring(startTime.indexOf(':') + 1, startTime.length()))),
						hourb, 
						Integer.parseInt((endTime.substring(endTime.indexOf(':') + 1, endTime.length()))),
						name);
				t.startTime = startTime;
				t.endTime = endTime;
				schedule.add(t);
			}
			schedules.add(schedule);
			Log.d("schedules",schedules.toString()+"scheduless");
		}
		return schedules;
	}

	public static class ScheduleToken {
		String name;
		int startHour, startMinute, endHour, endMinute;
		String startTime;
		String endTime;

		public ScheduleToken(int startHour, int startMinute, int endHour, int endMinute, String name) {
			this.name = name;
			this.startHour = startHour;
			this.startMinute = startMinute;
			this.endHour = endHour;
			this.endMinute = endMinute;
		}
		public boolean checkInside(int hour, int minute) {
			return (startHour*60 + startMinute <= hour*60 + minute
					&& hour*60 + minute < endHour*60 + endMinute);
		}
		public int percent(int hour, int minute) {
			if(!checkInside(hour, minute)) {
				return -1;
			} else {
				int total = endMinute - startMinute + 60*(endHour - startHour);
				int completed = minute - startMinute + 60*(hour - startHour);
				return (int)((double)completed/total*100);
			}
		}
	}


	public class ScheduleAdapter extends BaseAdapter {

		ArrayList<ScheduleToken> schedule;
		View[] views;
		public ScheduleAdapter(ArrayList<ScheduleToken> schedule) {
			this.schedule = schedule;
			views = new View[schedule.size()];
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = getLayoutInflater().inflate(R.layout.schedule_item_layout, null);
			ScheduleToken token = schedule.get(position);
			TextView period = (TextView)item.findViewById(R.id.item_period);
			TextView time1 = (TextView)item.findViewById(R.id.item_time1);
			TextView time2 = (TextView)item.findViewById(R.id.item_time2);
			ProgressBar progress = (ProgressBar)item.findViewById(R.id.item_progress);
			period.setText(token.name);
			 if(Integer.parseInt(token.startTime.replace(":",""))<700){
				 	time1.setText(token.startTime+"pm");
				} else if(Integer.parseInt(token.startTime.replace(":",""))>=1200){
					time1.setText(token.startTime+"pm");
				}
				else {
					time1.setText(token.startTime+"am");
				}
					
			
			 if(Integer.parseInt(token.endTime.replace(":",""))<700){
				    time2.setText(token.endTime+"pm");
				} else if(Integer.parseInt(token.endTime.replace(":",""))>=1200){
					time2.setText(token.endTime+"pm");
				}
				else {
					time2.setText(token.endTime+"am");
				}
					
			
			
			
			
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int percent = token.percent(hour, minute);
			if(percent >= 0) {
				progress.setVisibility(View.VISIBLE);
				progress.setProgress(percent);
			} else {
				progress.setVisibility(View.INVISIBLE);
			}
			views[position] = item;
			return item;
		}

		@Override
		public final int getCount() {
			return schedule.size();
		}

		@Override
		public Object getItem(int position) {
			return views[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long id) {
		try {
			scheduleList.setAdapter(new ScheduleAdapter(schedules.get(spinner.getSelectedItemPosition())));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
