package com.example.dds_publisher;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.util.Log;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;
import com.toc.coredx.DDS.DDS;
import com.toc.coredx.DDS.DataWriterListener;
import com.toc.coredx.DDS.DataWriterQos;
import com.toc.coredx.DDS.DomainParticipant;
import com.toc.coredx.DDS.DomainParticipantFactory;
import com.toc.coredx.DDS.DomainParticipantQos;
import com.toc.coredx.DDS.DynamicType;
import com.toc.coredx.DDS.DynamicTypeDataWriter;
import com.toc.coredx.DDS.LongDynamicType;
import com.toc.coredx.DDS.Publisher;
import com.toc.coredx.DDS.PublisherListener;
import com.toc.coredx.DDS.PublisherQos;
import com.toc.coredx.DDS.ReturnCode_t;
import com.toc.coredx.DDS.StringDynamicType;
import com.toc.coredx.DDS.StructDynamicType;
import com.toc.coredx.DDS.Topic;
import com.toc.coredx.DDS.TypeSupport;
import com.example.dds_publisher.Writer;;

public class DDS_Publisher extends Activity {
	public static StructDynamicType        tablet_type   = null;
    public static DynamicTypeDataWriter    writers_tablet      = null;
    public static DynamicTypeDataWriter[]  writers = null;
    public static DomainParticipantQos     dp_qos_tablet       = new DomainParticipantQos();
    public static Publisher                pub_tablet          = null;
    public static PublisherQos             pub_qos_tablet      = null;
    public static PublisherListener 	   pub_listener_tablet = null;
    public static Topic	                   topics              = null;
    public static MulticastLock            mcastLock = null;
    public static Vector<Writer> publisher = null;
    public StringDynamicType testing = null;    
    public int XVel_DDS;
	public int YVel_DDS;
	public int CompassDir_DDS;
	public int GPS_LN_DDS;
	public int GPS_LT_DDS;
	dataDDS dataPublish;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    
    	//Enable WiFi Communication
    	 WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
          mcastLock = wifi.createMulticastLock("Tablet");
          mcastLock.acquire();
          
    	// open CoreDX DDS license file:
        BufferedReader br = null;
        String license = new String("<");
        try {
        	Log.i("Debug", "...Opening License");
          br = new BufferedReader(new InputStreamReader(this.getAssets().open("coredx_dds.lic")));
        } catch (IOException e) {
        	Log.i("Debug", "...License did not open");
          Log.e("Tablet", e.getMessage());
        }
        if (br!=null)
          {
    	String ln;
    	try {
    	  while ((ln = br.readLine()) != null) {
    	    license = new String(license + ln + "\n");
    	  }
    	} catch (IOException e) {
    	  Log.e("Tablet", e.getMessage());
    	}
          }
        license = new String(license + ">");
        Log.i("Tablet", "...License seems to be good");
	    
        //Tablet variables
		Log.i("Tablet","STARTING -------------------------");
	    
		//Declare Domain Participant Factory
		DomainParticipantFactory dpf = DomainParticipantFactory.get_instance();
	    
		//Set Coredx License to the Domain Participant Factory
		dpf.set_license(license);
	    Log.i("Tablet", "Creating Participant");
	    System.out.println("CREATE PARTICIPANT ---------------"); 
	   
	    //Create DDS entities for reading tablet data
	    DomainParticipant dp = null;
	    dp = dpf.create_participant(0, null, null, 0);
        if(dp == null)
        {
        	//failed to create DomainParticipant -- bad license
        	android.util.Log.e("CoreDX DDS", "Unable to create Tablet DomainParticipant.");
        	new AlertDialog.Builder(this)
      	  .setTitle("CoreDX DDS Shapes Error")
      	  .setMessage("Unable to create Tablet DomainParticipant.\n(Bad License?)")
      	  .setNeutralButton("Close", new DialogInterface.OnClickListener() {
      	      public void onClick(DialogInterface dlg, int s) { /* do nothing */ } })
      	  .show();
        }
        
        //Create Publisher for domain participant
        pub_tablet = dp.create_publisher(pub_qos_tablet, pub_listener_tablet, 0);
    	
        Log.i("Tablet","creating publisher");
        
    	System.out.println("REGISTERING TYPE -----------------");
	    
	    tablet_type = new StructDynamicType();
    	tablet_type.set_num_fields(5);
    	tablet_type.set_field(0, "XVel_DDS",new LongDynamicType(), false);
        tablet_type.set_field(1, "YVel_DDS", new LongDynamicType(), false);
        tablet_type.set_field(2, "CompassDir_DDS", new LongDynamicType(), false);
        tablet_type.set_field(3, "GPS_LN_DDS", new LongDynamicType(), false);
        tablet_type.set_field(4, "GPS_LT_DDS", new LongDynamicType(), false);
	    
        //Declare and register type support
        System.out.println("Declare TS -----------------");
        
        TypeSupport ts = StructDynamicType.create_typesupport(tablet_type);
       
        
        System.out.println("TS Declared -----------------");
    	ReturnCode_t retval = ts.register_type(dp, ts.get_type_name());
   
    	System.out.println("CREATE TOPIC ---------------------");
	    
    	topics= dp.create_topic("helloTopic", ts.get_type_name(), 
	    		DDS.TOPIC_QOS_DEFAULT,
	    	//	null, //default
	    		null,
	    		0);
	    
	    System.out.println("CREATE PUBLISHER -----------------");
	    DataWriterQos dw_qos_tablet = new DataWriterQos();
  		pub_tablet.set_default_datawriter_qos(dw_qos_tablet);
  	    dw_qos_tablet.entity_name.value = "JAVA_DW";
  	    DataWriterListener dw_listener = null;                                                                                                                                                                                                                                                                                                                                                                                                                                   
	    
  	    System.out.println("CREATE DATAWRITER ----------------");
	    
  	    
  	    DynamicTypeDataWriter dw = (DynamicTypeDataWriter)pub_tablet.create_datawriter(topics, 
                dw_qos_tablet,
                dw_listener, 
                0);

	  /*
  	    newWriter(dw,XVel_DDS,
  	      		YVel_DDS,
  	    		CompassDir_DDS,
  	    		GPS_LN_DDS,
  	    		GPS_LT_DDS);
  	    */
	    System.out.println("DATAWRITER CREATED ----------------");
        
	    if(dw == null)
        {
          System.out.println("ERROR creating data writer\n");
          return;
         }
	    
	    int i = 1; 
	    while ( true ) {
	   
        retval = dw.write(tablet_type, null); 
	    System.out.println( "DDS_DataWriter_write() " + retval);

	    if ( retval != ReturnCode_t.RETCODE_OK )
		System.out.println( "   ====  DDS_DataWriter_write() error... ");
	      try {
		Thread.currentThread().sleep(10000);   // 1 second sleep
	      } catch (Exception e) {
		e.printStackTrace();
	      }

	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dds__publisher);
    }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dds__publisher, menu);
        return true;
    }
    
    
    public static void newWriter(DynamicTypeDataWriter dw,
    		int XVel_DDS,
    		int YVel_DDS,
    		int CompassDir_DDS,
    		int GPS_LN_DDS,
    		int GPS_LT_DDS)
    {
		XVel_DDS = 666;
		YVel_DDS = 666;
		CompassDir_DDS = 124;
		GPS_LN_DDS = 12;
		GPS_LT_DDS = 123;
		
    	Writer w = new Writer(dw, 
  			  XVel_DDS,
  			  YVel_DDS,
  			  CompassDir_DDS,
  			  GPS_LN_DDS,
  			  GPS_LT_DDS);
  	    
 	    synchronized (DDS_Publisher.publisher) { DDS_Publisher.publisher.add(w); }
    }

    /** call to move and publish any local shapes  */
    public static void publishLocal(DynamicTypeDataWriter dw)
    {
      synchronized(DDS_Publisher.publisher) {
        if (DDS_Publisher.publisher != null)
  	{
  	  Iterator<Writer> iter = DDS_Publisher.publisher.iterator();
  	  for (; iter.hasNext(); )
  	    {
  	      Writer w = iter.next();
  	     // w.updatePosition();
  	    System.out.println("NEWWRITER CREATED ----------------");
  	      w.publish();
  	      
  	    }
  	}
      }
    }
}
