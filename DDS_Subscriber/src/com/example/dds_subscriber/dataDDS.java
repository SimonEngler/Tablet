package com.example.dds_subscriber;


//CoreDX DDL Generated code.  Do not modify - modifications may be overwritten.



public class dataDDS {

//instance variables
public float XVel_DDS;
public float YVel_DDS;
public float CompassDir_DDS;
public float GPS_LN_DDS;
public float GPS_LT_DDS;

//constructors
public dataDDS() {}
public dataDDS( float __f1, float __f2, float __f3, float __f4, float __f5 ) {
XVel_DDS = __f1;
YVel_DDS = __f2;
CompassDir_DDS = __f3;
GPS_LN_DDS = __f4;
GPS_LT_DDS = __f5;
}

public void clear() {
}

public void copy( dataDDS from ) {
this.XVel_DDS = from.XVel_DDS;
this.YVel_DDS = from.YVel_DDS;
this.CompassDir_DDS = from.CompassDir_DDS;
this.GPS_LN_DDS = from.GPS_LN_DDS;
this.GPS_LT_DDS = from.GPS_LT_DDS;
}

}; // dataDDS