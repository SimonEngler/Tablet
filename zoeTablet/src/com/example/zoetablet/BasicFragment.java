package com.example.zoetablet;


import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BasicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        // onCreateView() is a lifecycle event that is unique to a Fragment. This is called when Android
        // needs the layout for this Fragment. The call to LayoutInflater::inflate() simply takes the layout
        // ID for the layout file, the parent view that will hold the layout, and an option to add the inflated
        // view to the parent. This should always be false or an exception will be thrown. Android will add
        // the view to the parent when necessary.
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        String fileName = "/storage/sdcard0/DCIM/Camera/robot.jpg";
           Bitmap myBitmap = BitmapFactory.decodeFile(fileName);
          ImageView jpgView = (ImageView)view.findViewById(R.id.demoImageView);
          jpgView.setImageBitmap(myBitmap);
          
        // This is how you access your layout views. Notice how we call the findViewById() method
        // on our View directly. There is no method called findViewById() defined on Fragments like
        // there is in an Activity.
        //Button button = (Button) view.findViewById(R.id.fragment_button);
        
        // A simple OnClickListener for our button. You can see here how a Fragment can encapsulate
        // logic and views to build out re-usable Activity components.
        //button.setOnClickListener(new OnClickListener() {
            
        //    public void onClick(View v) {
        //        Activity activity = getActivity();
        //        
        //        if (activity != null) {
        //            Toast.makeText(activity, R.string.toast_you_just_clicked_a_fragment, Toast.LENGTH_LONG).show();
        //        }
        //    }
        //    
        //});
        
        return view;
    }

}
