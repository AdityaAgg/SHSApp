package com.appdev.shsappp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentID extends Activity {
	static final int REQUEST_IMAGE_CAPTURE = 1;
	int imageHeight;
	int imageWidth;
	String mCurrentPhotoPath;
	File photoFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_id);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ImageView photo_id = (ImageView) findViewById(R.id.image_student_id);

		imageHeight = 300;
		imageWidth = 500;
		//Log.d("he", "herrere" + imageHeight);
		//Log.d("he", "herrere" + imageWidth);
		photo_id.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				// Ensure that there's a camera activity to handle the intent
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					// Create the File where the photo should go

					try {
						photoFile = createImageFile();
					} catch (IOException ex) {
						// Error occurred while creating the File

					}
					// Continue only if the File was successfully created
					if (photoFile != null) {
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(photoFile));
						startActivityForResult(takePictureIntent,
								REQUEST_IMAGE_CAPTURE);
					}
				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			FileOutputStream fos;
			ImageView photo_id = (ImageView) findViewById(R.id.image_student_id);
			Bitmap bitmaper = decodeSampledBitmapFromResource(mCurrentPhotoPath);
			photo_id.setImageBitmap(bitmaper);
			try {
				String FILENAME = "photoiders22222222.txt";
				String string = mCurrentPhotoPath;
				//Log.d("Writing", "writingwritingwriting");
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(string.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (photoFile != null) {
				try {
					fos = new FileOutputStream(photoFile);
					bitmaper.compress(Bitmap.CompressFormat.PNG, 100, fos);
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				TextView add_photo_text = (TextView) findViewById(R.id.add_photo_id_request);
				add_photo_text.setText("Click to Change Photo");

			}

		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/saved_images");
		myDir.mkdirs();
		File image = new File(myDir, /* prefix */imageFileName + ".jpg");

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	public Bitmap decodeSampledBitmapFromResource(String picturePath) {
		ImageView photo_id = (ImageView) findViewById(R.id.image_student_id);
		// imageWidth = photo_id.getWidth();
		/*
		 * if (imageWidth==0){ imageWidth=20; }
		 */

		// imageHeight = photo_id.getHeight();
		// if (imageHeight==0){
		// imageHeight=20;
		// }
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, options);
		int photoW = options.outWidth;
		int photoH = options.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / imageWidth, photoH / imageHeight);
		options.inJustDecodeBounds = false;
		options.inSampleSize = scaleFactor;
		options.inPurgeable = true;
		FileOutputStream fos = null;

		return BitmapFactory.decodeFile(picturePath, options);
	}

	@Override
	protected void onStart() {
		super.onStart();
		ImageView photo_id = (ImageView) findViewById(R.id.image_student_id);
		FileInputStream fis;
		String firstLine = "";
		try {
			fis = openFileInput("photoiders22222222.txt");
			Scanner scanner = new Scanner(fis);
			firstLine = scanner.nextLine();
			scanner.close();
			//Log.d("passedthrough", "passedthrough" + firstLine);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		firstLine.trim();

		if (!(firstLine == null || firstLine == "")) {
			mCurrentPhotoPath = firstLine;
			photo_id.setImageBitmap(decodeSampledBitmapFromResource(mCurrentPhotoPath));
			TextView add_photo_text = (TextView) findViewById(R.id.add_photo_id_request);
			add_photo_text.setText("Click to Change Photo");
		}
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
			return true;
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

}
