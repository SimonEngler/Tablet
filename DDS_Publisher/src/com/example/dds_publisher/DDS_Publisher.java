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
import java.util.Random;
import java.util.Vector;
import com.toc.coredx.DDS.DDS;
import com.toc.coredx.DDS.DataWriterListener;
import com.toc.coredx.DDS.DataWriterQos;
import com.toc.coredx.DDS.DomainParticipant;
import com.toc.coredx.DDS.DomainParticipantFactory;
import com.toc.coredx.DDS.DomainParticipantQos;
import com.toc.coredx.DDS.DynamicTypeDataWriter;
import com.toc.coredx.DDS.Publisher;
import com.toc.coredx.DDS.PublisherListener;
import com.toc.coredx.DDS.PublisherQos;
import com.toc.coredx.DDS.ReturnCode_t;
import com.toc.coredx.DDS.StructDynamicType;
import com.toc.coredx.DDS.Topic;
import com.example.dds_publisher.Writer;

//Import DDS data constructors
import com.example.dds_publisher.dataDDS;
import com.example.dds_publisher.dataDDSDataReader;
import com.example.dds_publisher.dataDDSDataWriter;
import com.example.dds_publisher.dataDDSSeq;
import com.example.dds_publisher.dataDDSTypeSupport;
import java.util.Random;
import java.text.DecimalFormat;
import java.util.Vector;

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
    
    
    //Declare DDS Variables
	dataDDS dataMessage;
	dataDDSDataReader dataDataReader;
	dataDDSDataWriter dataWriter;
	dataDDSSeq dataSeq;
	dataDDSTypeSupport dataTypeSup;
	ReturnCode_t returnValue;
	Random generator = new Random();
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    
    
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
        dataDDSTypeSupport ts = new dataDDSTypeSupport();
        ReturnCode_t returnValue = ts.register_type(dp, null);
    	
    	if(returnValue != ReturnCode_t.RETCODE_OK)
    	{
    	  System.out.println("ERROR registering type\n");
    	  return;
    	 }
    	
    	System.out.println("CREATE TOPIC ---------------------");
    	  /* create a DDS Topic with the FilterMsg data type. */
    	topics= dp.create_topic("dataDDS",ts.get_type_name(), 
	    		DDS.TOPIC_QOS_DEFAULT,
	    		null,
	    		0);
    	
    	if(topics == null)
    	{
    		System.out.println("Error creating topic");
    		return;
    	}
	    
	    System.out.println("CREATE PUBLISHER -----------------");
	    DataWriterQos dw_qos_tablet = new DataWriterQos();
  		pub_tablet.set_default_datawriter_qos(dw_qos_tablet);
  	    dw_qos_tablet.entity_name.value = "JAVA_DW";
  	    DataWriterListener dw_listener = null;                                                                                                                                                                                                                                                                                                                                                                                                                                   
	    System.out.println("CREATE DATAWRITER ----------------");
	   
  	    //Create DDS Data writer
  	    dataDDSDataWriter dw = (dataDDSDataWriter) pub_tablet.create_datawriter(topics, 
                DDS.DATAWRITER_QOS_DEFAULT,
                dw_listener, 
                0);
  	    
  	    //Cheack to see if DDS Data Writer worked 
	    if(dw == null)
        {
          System.out.println("ERROR creating data writer\n");
          return;
         }
	    System.out.println("DATAWRITER CREATED ----------------");
	    	     //Write the message to the DDS

	    
	    
	    
	    
	    
	    while ( true ) {
	    	System.out.println("initialize data...\n");
	 	    dataDDS dataMessage = new dataDDS();
	 		dataMessage.XVel_DDS = getDatalogValues(666);
	 		dataMessage.YVel_DDS = getDatalogValues(666);
	 		dataMessage.CompassDir_DDS = getDatalogValues(124);
	 		dataMessage.GPS_LN_DDS = getDatalogValues(12);
	 		dataMessage.GPS_LT_DDS = getDatalogValues(13);
	 		System.out.println("data ready...\n");
	    	
            returnValue = dw.write(dataMessage, null);
	    	//Check to see if message worked
		    if(returnValue != ReturnCode_t.RETCODE_OK )
		    {
		      System.out.println("ERROR writing sample\n");
			  return;
			}
		    System.out.println( "DDS_DataWriter_write() " + returnValue);
	      try {
		       Thread.currentThread().sleep(5000);   // 5 second sleep
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
    
	 public float getDatalogValues(float value)
	  {  
		 //Set the value
		 value = value*generator.nextFloat();
        value = Float.valueOf(twoDForm.format(value)); 
		 return value;
	  }
	  
    
    }
