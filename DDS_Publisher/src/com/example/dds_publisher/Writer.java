package com.example.dds_publisher;

import com.toc.coredx.DDS.*;

public class Writer {
	 
  public LongDynamicType XVel_DDS;
  public LongDynamicType YVel_DDS;
  public LongDynamicType CompassDir_DDS;  
  public LongDynamicType GPS_LN_DDS;
  public LongDynamicType GPS_LT_DDS;
  public DynamicTypeDataWriter dw;

  public Writer (DynamicTypeDataWriter[] dw, 
		  LongDynamicType XVel_DDS,
		  LongDynamicType YVel_DDS,
		  LongDynamicType CompassDir_DDS,
		  LongDynamicType GPS_LN_DDS,
		  LongDynamicType GPS_LT_DDS) {
  
	 
	 this.dw = dw[1];
	 this.XVel_DDS = XVel_DDS;
	 this.YVel_DDS = YVel_DDS;
	 this.CompassDir_DDS = CompassDir_DDS;
	 this.GPS_LN_DDS = GPS_LN_DDS;
	 this.GPS_LT_DDS = GPS_LN_DDS;
	 
  }

  public void updatePosition() {

  }
  

  public void publish() {
    // construct a 'data' sample 
		
	this.XVel_DDS = (LongDynamicType) DDS_Publisher.tablet_type.get_field(0);
    this.YVel_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(1);
	this.CompassDir_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(2);
	this.GPS_LN_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(3);
	this.GPS_LT_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(4);
   // XVel_DDS.set_long(this.XVel_DDS);
    
	dw.write(DDS_Publisher.tablet_type, null);
  }
};
