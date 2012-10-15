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
        
        // onCreateView() is a lifecycle event that is unique to a Fragment. This is called when Android
        // needs the layout for this Fragment. The call to LayoutInflater::inflate() simply takes the layout
        // ID for the layout file, the parent view that will hold the layout, and an option to add the inflated
        // view to the parent. This should always be false or an exception will be thrown. Android will add
        // the view to the parent when necessary.
        
        
  	    View view = inflater.inflate(R.layout.fragment_basic2, container, false);
  	    
  	    // This is how you access your layout views. Notice how we call the findViewById() method
        // on our View directly. There is no method called findViewById() defined on Fragments like
        // there is in an Activity.
        //  Button button = (Button) view.findViewById(R.id.fragment_button_2);
        
        // A simple OnClickListener for our button. You can see here how a Fragment can encapsulate
        // logic and views to build out re-usable Activity components.
        //  button.setOnClickListener(new OnClickListener() {
            
        //    public void onClick(View v) {
        //        Activity activity = getActivity();
                
        //        if (activity != null) {
        //            Toast.makeText(activity, R.string.toast_you_just_clicked_a_fragment_2, Toast.LENGTH_LONG).show();
        //        }
        //    }
            
       // });
   
       return view;
    }
    
  
}
