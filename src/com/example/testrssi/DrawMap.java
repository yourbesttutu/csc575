package com.example.testrssi;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class DrawMap extends View {
	private BitmapRegionDecoder mDecoder;
	/**
	 * 图片的宽度和高度
	 */
	private int mImageWidth, mImageHeight;
	/*private float cWidth1 = (float) (719 + 12 * 11.8);
	private float cHeight1 = 1265;
	private float cWidth2 = (float) (719 + 30 * 11.8);
	private float cHeight2 = (float) (1265 - (6 * 11.8));
	private float cWidth3 = (float) (719 + 54 * 11.8);
	private float cHeight3 = (float) (1265 - (6 * 11.8));*/
	private float cW1 = -100, cH1 = -100;
	private float cW2 = -100, cH2 = -100;
	private float cW3 = -100, cH3 = -100;
	float []r_min=new float[3];
	float []r_max=new float[3];
	private int[] X = new int[3];
	private int[] Y = new int[3];
	private boolean init=true;
	/**
	 * 绘制的区域
	 */
	private volatile Rect mRect = new Rect();

	private MoveGestureDetector mDetector;

	private static BitmapFactory.Options options = new BitmapFactory.Options();

	static {
		options.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	public void setInputStream(InputStream is,int x[],int y[],float r_min[], float r_max[]) {
		try {
			if(init==true){
			X=x;
			Y=y;
			this.r_min=r_min;
			this.r_max=r_max;
			init=false;
			}
			mDecoder = BitmapRegionDecoder.newInstance(is, false);
			BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
			// Grab the bounds for the scene dimensions
			tmpOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, tmpOptions);
			mImageWidth = tmpOptions.outWidth;
			mImageHeight = tmpOptions.outHeight;

			requestLayout();
			invalidate();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}
	}

	public void init() {
		mDetector = new MoveGestureDetector(getContext(),
				new MoveGestureDetector.SimpleMoveGestureDetector() {
					@Override
					public boolean onMove(MoveGestureDetector detector) {
						int moveX = (int) detector.getMoveX();
						int moveY = (int) detector.getMoveY();

						if (mImageWidth > getWidth()) {
							mRect.offset(-moveX, 0);
							checkWidth(moveX);
							invalidate();
						}
						if (mImageHeight > getHeight()) {
							mRect.offset(0, -moveY);
							checkHeight(moveY);
							invalidate();
						}

						return true;
					}
				});
	}

	private void checkWidth(int moveX) {

		Rect rect = mRect;
		int imageWidth = mImageWidth;
		int imageHeight = mImageHeight;
		if (rect.right > imageWidth) {
			rect.right = imageWidth;
			rect.left = imageWidth - getWidth();
		}
		if (rect.left < 0) {
			rect.left = 0;
			rect.right = getWidth();
		}

		cW1 = X[0] - rect.left;
		cW2 = X[1] - rect.left;
		cW3 = X[2] - rect.left;

	}

	private void checkHeight(int moveY) {

		Rect rect = mRect;
		int imageWidth = mImageWidth;
		int imageHeight = mImageHeight;

		if (rect.bottom > imageHeight) {
			rect.bottom = imageHeight;
			rect.top = imageHeight - getHeight();
		}

		if (rect.top < 0) {
			rect.top = 0;
			rect.bottom = getHeight();
		}

		cH1 = Y[0] - rect.top;
		cH2 = Y[1] - rect.top;
		cH3 = Y[2] - rect.top;

	}

	public DrawMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onToucEvent(event);
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap bm = mDecoder.decodeRegion(mRect, options);
		canvas.drawBitmap(bm, 0, 0, null);
		PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);
		Paint p1 = new Paint();
		p1.setColor(Color.RED);// 设置红色
		p1.setStyle(Style.STROKE);
		p1.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
		p1.setPathEffect(effects);  
		float strokeWidth = (float)(10);
        p1.setStrokeWidth(strokeWidth);
		Paint p2 = new Paint();
		p2.setColor(Color.BLUE);// 设置红色
		p2.setStyle(Style.STROKE);
		p2.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
		p2.setStrokeWidth(strokeWidth);
		//p2.setPathEffect(effects);
		Paint p3 = new Paint();
		p3.setColor(Color.BLACK);// 设置红色
		
		p3.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
	
		canvas.drawCircle(cW1, cH1, 10, p3);
		canvas.drawCircle(cW2, cH2, 10, p3);
		canvas.drawCircle(cW3, cH3, 10, p3);
		canvas.drawCircle(cW1, cH1, (float) (r_min[0]*14.75), p1);
		canvas.drawCircle(cW1, cH1, (float) (r_max[0]*14.75), p2);
		canvas.drawCircle(cW2, cH2, (float) (r_min[1]*14.75), p1);
		canvas.drawCircle(cW2, cH2, (float) (r_max[1]*14.75), p2);
		canvas.drawCircle(cW3, cH3, (float) (r_min[2]*14.75), p1);
		canvas.drawCircle(cW3, cH3, (float) (r_max[2]*14.75), p2);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		int imageWidth = mImageWidth;
		int imageHeight = mImageHeight;

		// 默认直接显示图片的中心区域，可以自己去调节
		mRect.left = imageWidth / 2 - width / 2;
		mRect.top = imageHeight / 2 - height / 2;
		mRect.right = mRect.left + width;
		mRect.bottom = mRect.top + height;

	}

}
