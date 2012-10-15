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


