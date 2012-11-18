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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BasicFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
  	    View view = inflater.inflate(R.layout.fragment_basic2, container, false);
  	   
       return view;
    }
    
  
}
