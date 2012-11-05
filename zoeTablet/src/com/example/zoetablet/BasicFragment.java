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
        
        
          View view = inflater.inflate(R.layout.fragment_basic, container, false); 
          byte[] imageBuffer = null;
          imageBuffer = getPicture(BasicFragmentActivity.data_image_DDS);
          Bitmap bitmap = BitmapFactory.decodeByteArray(imageBuffer, 0, imageBuffer.length);
          ImageView jpgView = (ImageView)view.findViewById(R.id.demoImageView);
          jpgView.setImageBitmap(bitmap);

        return view;
    }
    
    public byte[] getPicture(byte[] buffer)
    {
    	return buffer;
    }

}
