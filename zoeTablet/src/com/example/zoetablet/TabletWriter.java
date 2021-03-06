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


import com.example.zoetablet.BasicFragmentActivity;
import com.toc.coredx.DDS.*;
import java.lang.String;
import java.util.Random;

public class TabletWriter {

		  
		  public double XVel_DDS;
		  public double YVel_DDS;
		  public double CompassDir_DDS;
		  public double GPS_LN_DDS;
		  public double GPS_LT_DDS;
		  public String Log_DDS;
		  public byte[] data_image_DDS;
		  public DynamicTypeDataWriter dw_DDS;
		  Random generator = new Random();

		  public TabletWriter(DynamicTypeDataWriter dw_DDS,double XVel_DDS,double YVel_DDS,double CompassDir_DDS,
				  double GPS_LN_DDS, double GPS_LT_DDS, String Log_DDS, byte[] data_image_DDS) {
			  
		    this.dw_DDS    = dw_DDS;
		    this.XVel_DDS = XVel_DDS;
		    this.YVel_DDS = YVel_DDS;
		    this.CompassDir_DDS = CompassDir_DDS;
		    this.GPS_LN_DDS = GPS_LN_DDS;
		    this.GPS_LT_DDS = GPS_LT_DDS;
		    this.Log_DDS = Log_DDS;
		    this.data_image_DDS = data_image_DDS;
		   
		  }
		  
		  public void updateData(){
			  
			  this.XVel_DDS = XVel_DDS*generator.nextDouble() + 666.;
			  this.YVel_DDS = YVel_DDS*generator.nextDouble() +666.;
			  this.CompassDir_DDS = CompassDir_DDS*generator.nextDouble() +666.;
			  this.GPS_LN_DDS = GPS_LN_DDS*generator.nextDouble() +666.;
			  this.GPS_LT_DDS = GPS_LT_DDS*generator.nextDouble() +666.;
			  
		  }


		  public void publish() {
			  
			 
	       DoubleDynamicType XVel_DDS = (DoubleDynamicType)BasicFragmentActivity.tablet_type.get_field(0);
	       DoubleDynamicType YVel_DDS = (DoubleDynamicType)BasicFragmentActivity.tablet_type.get_field(1);
	       DoubleDynamicType CompassDir_DDS = (DoubleDynamicType)BasicFragmentActivity.tablet_type.get_field(3);
	       DoubleDynamicType GPS_LN_DDS = (DoubleDynamicType)BasicFragmentActivity.tablet_type.get_field(4);
	       DoubleDynamicType GPS_LT_DDS = (DoubleDynamicType)BasicFragmentActivity.tablet_type.get_field(5);
	       
	       //Do I need to set these before publishing?
	       XVel_DDS.set_double(this.XVel_DDS);
	       YVel_DDS.set_double(this.YVel_DDS);
	       CompassDir_DDS.set_double(this.CompassDir_DDS);
	       GPS_LN_DDS.set_double(this.GPS_LN_DDS);
	       GPS_LT_DDS.set_double(this.GPS_LT_DDS);
	       dw_DDS.write(BasicFragmentActivity.tablet_type, null);
		 
		  }
};

