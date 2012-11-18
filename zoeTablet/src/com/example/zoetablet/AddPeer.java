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

import com.example.zoetablet.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.Spanned;


public class AddPeer extends FragmentActivity {
  EditText et_addr;
  EditText et_idx;

  public AddPeer() {
    // TODO Auto-generated constructor stub
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addpeer);

    Log.i("Shapes","AddPeer onCreate...");

    et_addr = (EditText) findViewById(R.id.peerAddress);
    et_idx = (EditText) findViewById(R.id.peerIndex);

    // construct an InputFilter for IPv4 addresses
    InputFilter[] filters = new InputFilter[1];
    filters[0] = new InputFilter() {
        @Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
	{
	  if (end > start) 
	    {
	      String destTxt = dest.toString();
	      String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
	      if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) 
		{ 
		  return "";
		} 
	      else 
		{
		  String[] splits = resultingTxt.split("\\.");
		  for (int i=0; i<splits.length; i++) 
		    {
		      if (Integer.valueOf(splits[i]) > 255) 
			{
			  return "";
                        }
                    }
                }
            }
	  return null;
        }
    };
    et_addr.setFilters(filters);

    // add callbacks to buttons... 
    ((Button)findViewById(R.id.addPeerButton)).setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {

	  // read results... if valid, construct 'return' data 
	  String addr = et_addr.getText().toString();
	  if (addr.length() > 0)
	    {
	      Intent result = new Intent();
	      result.putExtra("addr", addr);
	      result.putExtra("idx_start", et_idx.getText().toString());
	      result.putExtra("idx_end", et_idx.getText().toString());
	      setResult(RESULT_OK, result);
	    }
	  else 
	    {
	      setResult(RESULT_CANCELED);
	    }
	  finish();
	}
      });
  }

  /** called when activity starts */
  @Override
  public void onStart() {	
    super.onStart();
    Log.i("Shapes","AddPeer onStart...");
  }
  /** called when activity resumes */
  @Override
  public void onResume() {
    super.onResume();
    Log.i("Shapes","AddPeer onResume...");
  }
  /** called when activity pauses */
  @Override
  public void onPause() {
    super.onPause();
    Log.i("Shapes","AddPeer onPause...");
  }
  /** called when activity stops */
  @Override
  public void onStop() {
    super.onStop();
    Log.i("Shapes","AddPeer onStop...");
  }
  /** called when activity is destroyed */
  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i("Shapes","AddPeer onDestroy...");
  }
}

