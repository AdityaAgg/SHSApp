package com.appdev.shsappp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class SportsCenterActivity extends Activity {
	
	public static Context context;
	
	private LinearLayout main;
	
	private LinearLayout choosers;
	private Spinner sportSpinner;
	private Spinner leagueSpinner;
	private DatePicker datePicker;
	
	private Button goButton;
	
	public static ScrollView scroll;
	private TextView scrollText;
	
	public static Runnable scrollerDowner = new Runnable() {
        @Override
        public void run() {
            scroll.fullScroll(ScrollView.FOCUS_DOWN);
        }
    };
    
    private static ArrayList<String> sports;
    private static HashMap<String, String[]> leagues;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize leagues and sports
    	leagues = new HashMap<String,String[]>();
    	sports = new ArrayList<String>();
    	
    	leagues.put("SELECT SPORT...", new String[] {"SELECT LEAGUE..."}); sports.add("SELECT SPORT...");
    	leagues.put("Badminton", new String[] {"SELECT LEAGUE...", "V", "JV"}); sports.add("Badminton");
    	leagues.put("Baseball", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Boys Freshman"}); sports.add("Baseball");
    	leagues.put("Basketball", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Basketball");
    	leagues.put("Cheerleading", new String[] {"N/A"}); sports.add("Cheerleading");
    	leagues.put("Cross Country", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Cross Country");
    	leagues.put("Dance", new String[] {"N/A"}); sports.add("Dance");
    	leagues.put("Field Hockey", new String[] {"SELECT LEAGUE...", "Girls V", "Girls JV"}); sports.add("Field Hockey");
    	leagues.put("Football", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO"}); sports.add("Football");
    	leagues.put("Golf", new String[] {"SELECT LEAGUE...", "Boys V", "Girls V"}); sports.add("Golf");
    	leagues.put("Lacrosse", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Lacrosse");
    	leagues.put("Soccer", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Soccer");
    	leagues.put("Softball", new String[] {"SELECT LEAGUE...", "Girls V", "Girls JV"}); sports.add("Softball");
    	leagues.put("Swimming", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Swimming");
    	leagues.put("Tennis", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Tennis");
    	leagues.put("Track and Field", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Track and Field");
    	leagues.put("Volleyball", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV", "Girls Freshman"}); sports.add("Volleyball");
    	leagues.put("Water Polo", new String[] {"SELECT LEAGUE...", "Boys V", "Boys F-SO", "Girls V", "Girls JV"}); sports.add("Water Polo");
    	leagues.put("Wrestling", new String[] {"SELECT LEAGUE...", "Boys V", "Boys JV"}); sports.add("Wrestling");
    	
    	
    	
        
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable backgroundGradient = new GradientDrawable();
        backgroundGradient.setShape(GradientDrawable.RECTANGLE);
        backgroundGradient.setColors(new int[]{0xffffffff,0xffff3333});
        backgroundGradient.setOrientation(Orientation.TOP_BOTTOM);
        main.setBackground(backgroundGradient);
        
       
    	////////spinners/DatePicker
        choosers = new LinearLayout(this);
        choosers.setOrientation(LinearLayout.HORIZONTAL);
        
        LinearLayout.LayoutParams chooserParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f);
        
        LinearLayout spinners = new LinearLayout(this);
        spinners.setOrientation(LinearLayout.VERTICAL);
        
    	sportSpinner = new Spinner(this);
    	sportSpinner.setLayoutParams(chooserParams);
    	ArrayAdapter<String> sportAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sports);
    	sportSpinner.setAdapter(sportAdapter);
    	sportSpinner.setSelection(sportAdapter.getPosition("SELECT SPORT..."));
    	sportSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> s, View v,
					int index, long id) {
				sportSelected(v);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    	spinners.addView(sportSpinner);
    	
    	leagueSpinner = new Spinner(this);
    	leagueSpinner.setLayoutParams(chooserParams);
    	ArrayAdapter<String> leagueAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, leagues.get(sportSpinner.getSelectedItem()));
    	leagueSpinner.setAdapter(leagueAdapter);
    	leagueSpinner.setSelection(0);
    	spinners.addView(leagueSpinner);
    	
    	choosers.addView(spinners);
    	
    	datePicker = new DatePicker(this);
    	datePicker.setLayoutParams(chooserParams);
    	datePicker.setCalendarViewShown(false);
    	choosers.addView(datePicker);
    	///////
        
        goButton = new Button(this);
        goButton.setText("GO");
        goButton.setBackgroundColor(0xffff0000);
        goButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goButtonClicked(v);
				//see method just after onCreate()
				
			}
		});
        
        scroll = new ScrollView(this);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        scrollParams.setMargins(20, 20, 20, 20);
        scroll.setLayoutParams(scrollParams);
     
        scrollText = new TextView(this);
        scrollText.setText("No timeline to display currently.");
        scrollText.setTextColor(0xff000000);
        scrollText.setSingleLine(false);
        scroll.addView(scrollText);

        main.addView(choosers);
        main.addView(goButton);
        main.addView(scroll);
        
        setContentView(main);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    public void goButtonClicked(View v){
    	
    	//checks internet connection. If none, Toast error and return
    	if(!isNetworkAvailable(context)){
    		Toast.makeText(context, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	//get info from 2 Spinners/DatePicker
    	String sport = ((String) sportSpinner.getSelectedItem()).trim().replaceAll("\\s+","_").toLowerCase();
    	String league = ((String) leagueSpinner.getSelectedItem()).trim().replaceAll("\\s+","_").toLowerCase();;
    	
    	String month = ""+(datePicker.getMonth()+1);
    	if(Integer.parseInt(month)<10)month="0"+month;
    	String day = ""+datePicker.getDayOfMonth();
    	if(Integer.parseInt(day)<10)day="0"+day;
    	String yr = ""+datePicker.getYear();
    	String date = month+"-"+day+"-"+yr;
    	
    	// if sportSpinner or leagueSpinner still have "select" then make notification saying make all selections first
    	if(sport.equals("select_sport...")){
    		Toast.makeText(context, "Please select a sport.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	else if(league.equals("select_league...")){
    		Toast.makeText(context, "Please select a league.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	setInfoOnTextView(scrollText, sport, league, date);
    	
    }
    
    private void sportSelected(View v){
    	String key = ((TextView)v).getText().toString();
    	ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, leagues.get(key));
    	leagueSpinner.setAdapter(a);
    	leagueSpinner.setSelection(0);
    }
    
    public static void scrollToBottom(){
    	//this is what causes the ScrollView to scroll automatically to the bottom once GO is pressed
    	scroll.post(scrollerDowner);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sports_center, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public static void setInfoOnTextView(final TextView v, final String sport, final String league, final String date){
		Thread newThread = new Thread(new Runnable(){

			@Override
			public void run() {
				
				final String message = getHTML(/*URL GOES HERE!!!*/"http://73.162.233.8:8080/requestdata.jsp?sport="+sport+"&league="+league+"&date="+date).replaceAll("%", "\n");

				v.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						v.setText(message);
						scrollToBottom();
						//this is what causes the ScrollView to scroll automatically to the bottom once GO is pressed
					}
					
				});
				
			}
			
		});
		newThread.start();
	}
	
	private static String getHTML(String url){
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String html = "";
		InputStream in = null;
		try {
			in = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		try {
			while((line = reader.readLine()) != null)
			{
			    str.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		html = str.toString();
		return html;
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
	}

}
