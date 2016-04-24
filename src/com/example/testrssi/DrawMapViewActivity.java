package com.example.testrssi;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class DrawMapViewActivity extends Activity {
	private DrawMap mImageView;
	float []r_min=new float[3];
	float []r_max=new float[3];
	private int[] X = new int[3];
	private int[] Y = new int[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_large_image_view);
		Intent intent = getIntent();
		r_min = intent.getFloatArrayExtra("r_min");
		r_max = intent.getFloatArrayExtra("r_max");
		X=intent.getIntArrayExtra("X");
		Y=intent.getIntArrayExtra("Y");
		for(int i=0;i<3;i++){
			X[i]=(int) ((X[i]*14.75)+355);
			Y[i]=(int) (919-(Y[i]*14.75));
		}
		//Toast.makeText(DrawMapViewActivity.this, "rssi for third location :"+rssi[2] + "X location for first location :"+ X[0] , Toast.LENGTH_LONG).show();
		mImageView = (DrawMap) findViewById(R.id.drawMap1);
		try {
			InputStream inputStream = getAssets().open("mapv6.png");
			mImageView.setInputStream(inputStream,X,Y,r_min,r_max);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
