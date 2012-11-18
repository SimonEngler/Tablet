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



import com.toc.coredx.DDS.*;

import java.util.Random;

public class WriterTablet {
  
	public String topic_tablet;
	public String Label;
	public double Xpos;
	public double Ypos;
	public double Velocity;
	public double GPS_LT;
	public double GPS_LN;
	public DynamicTypeDataWriter dw;
	//private Topic[] topics_tablet;
	  
	
	public WriterTablet(DynamicTypeDataWriter dw, Topic[] topics_tablet,
			double Xpos,double Ypos,double Velocity,double GPS_LT, double GPS_LN)
	{
		this.dw = dw;
		//this.topics_tablet = topics_tablet;
		//this.Label = Label;
		this.Xpos = Xpos;
		this.Ypos = Ypos;
		this.Velocity = Velocity;
		this.GPS_LT = GPS_LT;
		this.GPS_LN = GPS_LN;
	}
	
	 public void updateData() {
	    //Just have values vary randomly slightly for now
		Random generator = new Random();
		
		Xpos = Xpos + Xpos*generator.nextFloat();
		Ypos = Ypos + Ypos*generator.nextFloat();
		Velocity = Velocity + Velocity*generator.nextFloat();
		GPS_LT = GPS_LT + GPS_LT*generator.nextFloat();
		GPS_LN = GPS_LN + GPS_LN*generator.nextFloat();
		
	 }
}


