package com.example.zoetablet;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;



public class DisplayImage extends Fragment {
	
	//String path = Environment.getExternalStorageDirectory()+ "/Images/test.jpg";
	  @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		  Log.i("ImageDisplay","Showing Image");
		View view = inflater.inflate(R.layout.fragment_basic_picture, container, false);
		String fileName = "DCIM/Camera/Robot.jpg";
		File f = new File(Environment.getExternalStorageDirectory(), fileName); 
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		
		 FileInputStream is = null; 
		    try { 
		        is = new FileInputStream(f); 
		        Log.i("ImageDisplay",fileName);
		        
		    } catch (FileNotFoundException e) {
		        Log.i("ImageDisplay","Not Found" + fileName); 
		        return view; 
		    } 
		
		
	    
	   // String pathName = Environment.getExternalStorageDirectory().toString();
	    Bitmap bmp = BitmapFactory.decodeStream(is);
	    //ImageView img = null;
	    imageView.setImageBitmap(bmp);

	    return view;
	}
}	


