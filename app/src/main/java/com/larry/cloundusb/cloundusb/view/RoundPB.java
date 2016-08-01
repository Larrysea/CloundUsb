package com.larry.cloundusb.cloundusb.view;


import java.lang.ref.Reference;


import com.larry.cloundusb.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class RoundPB extends View{

	/**
	 * print
	 */
	private Paint paint;
	/**
	 * round's color
	 */
	private int roundColor;
	/**
	 * round progressbar's color
	 */
	private int roundProgressColor;
	/**
	 * the round's width
	 */
	private float roundWidth;
	/**
	 * the max progress
	 */
	private int max;
	/**
	 * the current progress
	 */
	private int progress;
	/**
	 * whether display the text
	 */
	private int style;
	
	private boolean clockWise;
	private boolean counterClockWise;
	private int startPoint;
	
	private Drawable centerIcon;
	private int centerIconId;
	
	private static final int STROKE=0;
	private static final int FILL=1;
	
	
	public RoundPB(Context context) {
		this(context,null);
	}

	public RoundPB(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	public RoundPB(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		
		paint=new Paint();
		TypedArray mTypedArray=context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
		roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.GRAY);
		roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		
		centerIconId=mTypedArray.getResourceId(R.styleable.RoundProgressBar_centerIcon, R.drawable.ic_menu_exit);
		centerIcon=context.getResources().getDrawable(centerIconId);
		mTypedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		int centre=getWidth()/2;
		int radius=(int)(centre-roundWidth/2);
		paint.setColor(roundColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(roundWidth);
		paint.setAntiAlias(true);
		canvas.drawCircle(centre, centre, radius, paint);
		
		paint.setStrokeWidth(0);
		int percent=(int)((float)progress/(float)max*100);
		float textWidth=paint.measureText(percent+"%");
		
//		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		BitmapDrawable db=(BitmapDrawable) centerIcon;
		Bitmap icon=db.getBitmap();
		canvas.drawBitmap(icon, centre -icon.getWidth()/2, centre-icon.getHeight()/2, paint);
		
		
		paint.setStrokeWidth(roundWidth);
		paint.setColor(roundProgressColor);
		RectF oval=new RectF(centre-radius,centre-radius,centre+radius,centre+radius);
		
		switch(style){
		case STROKE:
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270, -360*progress/max, false, paint);
			break;
		case FILL:
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress!=0)
			canvas.drawArc(oval, 270, -360*progress/max, true, paint);
			break;
		}
	}
	
	public synchronized int getMax(){
		return max;
	}
	
	public synchronized void setMax(int max){
		if(max<0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max=max;
	}
	public synchronized int getProgress(){
		return progress;
	}
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
		
	}
	
	
	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}


	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}
}
