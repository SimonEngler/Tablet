package com.example.zoetablet;


import com.example.zoetablet.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class DatalogFragment extends Fragment {
	
	public TextView tv_datalogFragment;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	     
		View view = inflater.inflate(R.layout.fragment_datalog, container, false);        
  	  
		
       return view;
    }

}
