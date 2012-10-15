package com.example.zoetablet;
import com.example.zoetablet.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



public class DualJoystickActivity extends Fragment {

	
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    
	    	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.dualjoystick, container, false);
	    	
	        
	       return view;
	    }
	    

	    
}