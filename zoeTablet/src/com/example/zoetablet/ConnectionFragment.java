package com.example.zoetablet;

import com.example.zoetablet.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class ConnectionFragment extends Fragment {
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        
	      
	        
	        
	  	    View view = inflater.inflate(R.layout.fragment_connection, container, false);
	  	   // Button button = (Button) view.findViewById(R.id.fragment_button_connection);
	        
	        // A simple OnClickListener for our button. You can see here how a Fragment can encapsulate
	        // logic and views to build out re-usable Activity components.
	       // button.setOnClickListener(new OnClickListener() {
	            
	           
	        //    public void onClick(View v) {
	        //        Activity activity = getActivity();
	                
	         //       if (activity != null) {
	        //            Toast.makeText(activity, R.string.toast_you_just_clicked_a_fragment, Toast.LENGTH_LONG).show();
	        //        }
	         //   }
	        //});
	            
	       return view;
	    
	  }
}