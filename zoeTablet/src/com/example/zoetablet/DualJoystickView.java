//Zoe Android Robot Interface (ZARI)
//
//Copyright (C) 2012 Carnegie Mellon University (CMU)
//All Rights Reserved.
//
//Permission to use, copy, modify and distribute this software and its
//documentation is hereby granted, provided that both the copyright notice and
//this permission notice appear in all copies of the software, derivative works
//or modified versions, and any portions thereof, and that both notices appear
//in supporting documentation.
//
//Carnegie Mellon University ALLOWS FREE USE OF THIS SOFTWARE IN ITS "AS IS" CONDITION.
//Carnegie Mellon University DISCLAIMS ANY LIABILITY OF ANY KIND FOR ANY DAMAGES
//WHATSOEVER RESULTING FROM THE USE OF THIS SOFTWARE.

//Any question, please contact the author:
//	Simon Engler
//	Sensor Networks Laboratory
//	Department of Electrical and Computer Engineering
//	University of Calgary
//	email: stengler@ucalgary.ca
//	       simon.engler@gmail.com

package com.example.zoetablet;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class DualJoystickView extends LinearLayout {
	public static final String TAG = DualJoystickView.class.getSimpleName();
	
	public final boolean D = false;
	public Paint dbgPaint1;

	public JoystickView stickL;
	public JoystickView stickR;

	public View pad;

	public DualJoystickView(Context context) {
		super(context);
		stickL = new JoystickView(context);
		stickR = new JoystickView(context);
		initDualJoystickView();
	}

	public DualJoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		stickL = new JoystickView(context, attrs);
		stickR = new JoystickView(context, attrs);
		initDualJoystickView();
	}

	private void initDualJoystickView() {
	//	setOrientation(LinearLayout.HORIZONTAL);
		
		if ( D ) {
			dbgPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
			dbgPaint1.setColor(Color.CYAN);
			dbgPaint1.setStrokeWidth(2);
			dbgPaint1.setStyle(Paint.Style.STROKE);
		}
		
		pad = new View(getContext());
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		removeView(stickL);
		removeView(stickR);

		float padW = getMeasuredWidth()-(getMeasuredHeight()*2);
		int joyWidth = (int) ((getMeasuredWidth()-padW)/2);
		LayoutParams joyLParams = new LayoutParams(joyWidth,getMeasuredHeight());
		
		stickL.setLayoutParams(joyLParams);
		stickR.setLayoutParams(joyLParams);
		
		stickL.TAG = "L";
		stickR.TAG = "R";
		stickL.setPointerId(JoystickView.INVALID_POINTER_ID);
		stickR.setPointerId(JoystickView.INVALID_POINTER_ID);

		addView(stickL);

		ViewGroup.LayoutParams padLParams = new ViewGroup.LayoutParams((int) padW,getMeasuredHeight());
		removeView(pad);
		pad.setLayoutParams(padLParams);
		addView(pad);
		
		addView(stickR);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		stickR.setTouchOffset(stickR.getLeft(), stickR.getTop());
	}
	
	public void setAutoReturnToCenter(boolean left, boolean right) {
		stickL.setAutoReturnToCenter(left);
		stickR.setAutoReturnToCenter(right);
	}
	
	public void setOnJostickMovedListener(JoystickMovedListener left, JoystickMovedListener right) {
		stickL.setOnJostickMovedListener(left);
		stickR.setOnJostickMovedListener(right);
	}
	
	public void setOnJostickClickedListener(JoystickClickedListener left, JoystickClickedListener right) {
		stickL.setOnJostickClickedListener(left);
		stickR.setOnJostickClickedListener(right);
	}
	
	public void setYAxisInverted(boolean leftYAxisInverted, boolean rightYAxisInverted) {
		stickL.setYAxisInverted(leftYAxisInverted);
		stickL.setYAxisInverted(rightYAxisInverted);
	}

	public void setMovementConstraint(int movementConstraint) {
		stickL.setMovementConstraint(movementConstraint);
		stickR.setMovementConstraint(movementConstraint);
	}

	public void setMovementRange(float movementRangeLeft, float movementRangeRight) {
		stickL.setMovementRange(movementRangeLeft);
		stickR.setMovementRange(movementRangeRight);
	}

	public void setMoveResolution(float leftMoveResolution, float rightMoveResolution) {
		stickL.setMoveResolution(leftMoveResolution);
		stickR.setMoveResolution(rightMoveResolution);
	}

	public void setUserCoordinateSystem(int leftCoordinateSystem, int rightCoordinateSystem) {
		stickL.setUserCoordinateSystem(leftCoordinateSystem);
		stickR.setUserCoordinateSystem(rightCoordinateSystem);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (D) {
			canvas.drawRect(1, 1, getMeasuredWidth()-1, getMeasuredHeight()-1, dbgPaint1);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
    	boolean l = stickL.dispatchTouchEvent(ev);
    	boolean r = stickR.dispatchTouchEvent(ev);
    	return l || r;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
    	boolean l = stickL.onTouchEvent(ev);
    	boolean r = stickR.onTouchEvent(ev);
    	return l || r;
	}
}
