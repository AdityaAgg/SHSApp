package com.appdev.shsappp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class LoadActivity extends Activity {

	
	private final Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.load_layout);
		final int welcomeScreenDisplay = 4000;

		/** create a thread to show splash up to splash time */
		Thread welcomeThread = new Thread() {

			int wait = 0;

			@Override
			public void run() {
				try {
					super.run();
					
					DirectoryDataSource datasource = new DirectoryDataSource(
							context);
					//Log.d("HiI;mrunning", "hiimrunning");
					boolean isNetwork = isNetworkAvailable();
					datasource.loadDatabase(isNetwork);
					/**
					 * use while to get the splash time. Use sleep() to increase
					 * the wait variable for every 100L.
					 */
					while (wait < welcomeScreenDisplay) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {
					System.out.println("EXc=" + e);
				} finally {
					/**
					 * Called after splash times up. Do some action after splash
					 * times up. Here we moved to another main activity class
					 */
					onFinish();
				}
			}
		};
		welcomeThread.start();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void onFinish() {
		Intent toMain = new Intent(this, MainActivity.class);
		startActivity(toMain);
		finish();
	}

}
