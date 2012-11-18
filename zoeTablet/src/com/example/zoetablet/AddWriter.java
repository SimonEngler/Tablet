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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.Activity;

import com.example.zoetablet.R;

public class AddWriter extends FragmentActivity {
  Activity addWriterActivity;
  ToggleButton rbsquare;
  ToggleButton rbcircle;
  ToggleButton rbtriangle;
  

  int          tb_ids[]    = { R.id.rbred, R.id.rborange, R.id.rbyellow, R.id.rbgreen, 
			       R.id.rbcyan, R.id.rbblue, R.id.rbmagenta, R.id.rbblack};
  ToggleButton tb_colors[] = { null, null, null, null, null, null, null, null };
  String       tb_name[]   = { "red", "orange", "yellow", "green", "cyan", "blue", "magenta", "black" };

  //public AddWriter() {
    // TODO Auto-generated constructor stub
  //}

  /** Called when the activity is first created. */
  @Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addwriter);
  }
 public View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
 {   
    Log.i("Shapes","AddWriter onCreate...");
    // add callbacks to buttons... PO
    
   
    rbsquare    = (ToggleButton) addWriterActivity.findViewById(R.id.rbsquare);
    rbcircle    = (ToggleButton) addWriterActivity.findViewById(R.id.rbcircle);
    rbtriangle  = (ToggleButton) addWriterActivity.findViewById(R.id.rbtriangle);
    
    rbsquare.setOnClickListener(new OnClickListener() { 
	@Override
	public void onClick(View v) { if (rbsquare.isChecked()) { rbcircle.setChecked(false); rbtriangle.setChecked(false); } }
      });
    
    rbcircle.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) { if (rbcircle.isChecked()) { rbsquare.setChecked(false); rbtriangle.setChecked(false); } }
      });
    
    rbtriangle.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) { if (rbtriangle.isChecked()) { rbsquare.setChecked(false); rbcircle.setChecked(false); } }
      });

    OnClickListener color_ocl = new OnClickListener() {
	@Override
	public void onClick(View v) {
	  ToggleButton tb = (ToggleButton)v;
	  if (tb.isChecked())
	    for (int i = 0;i < tb_colors.length; i++)
	      if (tb_colors[i] != tb) tb_colors[i].setChecked(false);
	}
      };

    for (int i = 0; i < tb_ids.length; i++)
      {
	tb_colors[i] = (ToggleButton)addWriterActivity.findViewById(tb_ids[i]);
	tb_colors[i].setOnClickListener(color_ocl);
      }

    ((Button)addWriterActivity.findViewById(R.id.addButton)).setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
	  // read results... if valid, construct 'return' data 
	  String shape = null;
	  if (rbsquare.isChecked())        shape = "Square";
	  else if (rbcircle.isChecked())   shape = "Circle";
	  else if (rbtriangle.isChecked()) shape = "Triangle";

	  int color_index = -1;
	  for (int i = 0; i<tb_colors.length; i++)
	    if (tb_colors[i].isChecked()) {
	      color_index = i;
	      break;
	    }
	  
	  if ((shape != null) && (color_index != -1)) {
	    Intent result = new Intent();
	    result.putExtra("shape", shape);
	    result.putExtra("color", tb_name[color_index]);
	    addWriterActivity.setResult(Activity.RESULT_OK, result);
	  }
	  else {
	    addWriterActivity.setResult(Activity.RESULT_CANCELED);
	  }
	  addWriterActivity.finish();
	}
      });
    return container;
    // all done callback: 
    // // finish, and open the 'add writer' view
    // public void onClick(View v) {
    //   Shapes.NewWriter("Square", "Green", 30);
    // }
  }


  /** called when activity starts */
  @Override
  public void onStart() {	
    super.onStart();
    Log.i("Shapes","AddWriter onStart...");
  }
  /** called when activity resumes */
  @Override
  public void onResume() {
    super.onResume();
    Log.i("Shapes","AddWriter onResume...");
  }
  /** called when activity pauses */
  @Override
  public void onPause() {
    super.onPause();
    Log.i("Shapes","AddWriter onPause...");
  }
  /** called when activity stops */
  @Override
  public void onStop() {
    super.onStop();
    Log.i("Shapes","AddWriter onStop...");
  }
  /** called when activity is destroyed */
  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i("Shapes","AddWriter onDestroy...");
  }
}
