package com.example.dds_publisher;

import com.toc.coredx.DDS.*;

public class Writer {
	 
  public int XVel_DDS;
  public int YVel_DDS;
  public int CompassDir_DDS;  
  public int GPS_LN_DDS;
  public int GPS_LT_DDS;
  public DynamicTypeDataWriter dw;

  public Writer (DynamicTypeDataWriter dw, 
		  int XVel_DDS,
		  int YVel_DDS,
		  int CompassDir_DDS,
		  int GPS_LN_DDS,
		  int GPS_LT_DDS) {
  
	 
	 this.dw = dw;
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
	LongDynamicType XVel_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(0);
    LongDynamicType YVel_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(1);
	LongDynamicType CompassDir_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(2);
	LongDynamicType GPS_LN_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(3);
	LongDynamicType GPS_LT_DDS = (LongDynamicType)DDS_Publisher.tablet_type.get_field(4);
    
	XVel_DDS.set_long(this.XVel_DDS);
	YVel_DDS.set_long(this.YVel_DDS);
	CompassDir_DDS.set_long(this.CompassDir_DDS);
	GPS_LT_DDS.set_long(this.GPS_LT_DDS);
	GPS_LN_DDS.set_long(this.GPS_LN_DDS);
    
	dw.write(DDS_Publisher.tablet_type, null);
  }
};
