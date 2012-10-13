package com.example.dds_subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.toc.coredx.DDS.DDS;
import com.toc.coredx.DDS.DataReader;
import com.toc.coredx.DDS.DataReaderListener;
import com.toc.coredx.DDS.DataReaderQos;
import com.toc.coredx.DDS.DataWriterQos;
import com.toc.coredx.DDS.DomainParticipant;
import com.toc.coredx.DDS.DomainParticipantFactory;
import com.toc.coredx.DDS.DomainParticipantQos;
import com.toc.coredx.DDS.DynamicType;
import com.toc.coredx.DDS.DynamicTypeDataReader;
import com.toc.coredx.DDS.DynamicTypeDataWriter;
import com.toc.coredx.DDS.DynamicTypeSeq;
import com.toc.coredx.DDS.LivelinessChangedStatus;
import com.toc.coredx.DDS.LongDynamicType;
import com.toc.coredx.DDS.Publisher;
import com.toc.coredx.DDS.PublisherQos;
import com.toc.coredx.DDS.RequestedDeadlineMissedStatus;
import com.toc.coredx.DDS.RequestedIncompatibleQosStatus;
import com.toc.coredx.DDS.ReturnCode_t;
import com.toc.coredx.DDS.SampleInfo;
import com.toc.coredx.DDS.SampleInfoSeq;
import com.toc.coredx.DDS.SampleLostStatus;
import com.toc.coredx.DDS.SampleRejectedStatus;
import com.toc.coredx.DDS.StringDynamicType;
import com.toc.coredx.DDS.StructDynamicType;
import com.toc.coredx.DDS.Subscriber;
import com.toc.coredx.DDS.SubscriberListener;
import com.toc.coredx.DDS.SubscriberQos;
import com.toc.coredx.DDS.SubscriptionMatchedStatus;
import com.toc.coredx.DDS.Topic;
import com.toc.coredx.DDS.TopicDescription;
import com.toc.coredx.DDS.TypeSupport;
import com.toc.coredx.DDS.coredx;
import com.toc.coredx.DDS.coredxConstants;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;

//Import DDS data constructors
import com.example.dds_subscriber.dataDDS;
import com.example.dds_subscriber.dataDDSDataReader;
import com.example.dds_subscriber.dataDDSDataWriter;
import com.example.dds_subscriber.dataDDSSeq;
import com.example.dds_subscriber.dataDDSTypeSupport;

public class DDS_Subscriber extends Activity {

	public static DynamicTypeDataReader[] string_dr = null;
	public static DynamicTypeDataReader[]  readers_tablet      = { null, null, null };
    public static DynamicTypeDataWriter[]  writers_tablet      = { null, null, null };
    public static DynamicTypeDataWriter[]  readers = null;
    DataReaderQos dr_qos_tablet = new DataReaderQos();
	DataWriterQos dw_qos_tablet = new DataWriterQos();
    public static DomainParticipantQos      dp_qos_tablet       = new DomainParticipantQos();
    public static Subscriber                sub_tablet          = null;
    public static SubscriberQos             sub_qos_tablet      = null;
    public static MulticastLock             mcastLock = null;
	public static StructDynamicType         tablet_type   = null;
//	public static StructDynamicType         samples   = null;
	
	//Declare DDS Message variables
	dataDDS dataMessage;
	dataDDSDataReader dataDataReader;
	dataDDSDataWriter dataWriter;
	dataDDSSeq dataSeq;
	dataDDSTypeSupport dataTypeSup;
	ReturnCode_t returnValue;
  	dataDDSSeq samples;
    SampleInfoSeq   samples_info;
	
	//FilterMsgPtrSeq       samples;
//	DDS_SampleInfoSeq     samples_info;
//	DDS_ReturnCode_t      retval;
//	DDS_SampleStateMask   ss = DDS_ANY_SAMPLE_STATE;
//	DDS_ViewStateMask     vs = DDS_ANY_VIEW_STATE;
//	DDS_InstanceStateMask is = DDS_ANY_INSTANCE_STATE;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
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
    
    	
   	 Log.i("Tablet", "Creating Subscriber");
     class TestDataReaderListener implements DataReaderListener 
     {

  	   //Is this variable right?
  		 TypeSupport ts;
  		
  		 
      @Override
  	public long get_nil_mask() { return 0; }

      @Override
  	public void on_requested_deadline_missed(DataReader dr,
  					       RequestedDeadlineMissedStatus status) { 
  	System.out.println(" @@@@@@@@@@@     REQUESTED DEADLINE MISSED    @@@@@"); 
  	System.out.println(" @@@@@@@@@@@                                  @@@@@" );
  	System.out.println(" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"); 
       };

       @Override
  	public void on_requested_incompatible_qos(DataReader dr,
  						RequestedIncompatibleQosStatus status) { 
  	System.out.println(" @@@@@@@@@@@     REQUESTED INCOMPAT QOS    @@@@@@@@"); 
  	System.out.println(" @@@@@@@@@@@        dr      = " + dr);
  	System.out.println(" @@@@@@@@@@@                               @@@@@@@@" );
  	System.out.println(" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"); 
       };

       @Override
  	public void on_sample_rejected(DataReader dr, 
  				     SampleRejectedStatus status) { 
       };

       @Override
  	public void on_liveliness_changed(DataReader dr,
  					LivelinessChangedStatus status)
       {
//  	TopicDescription   td = dr.get_topicdescription();
//  	System.out.println(" @@@@@@@@@@@     LIVELINESS CHANGED      @@@@@@@@@@"); 
 // 	System.out.println(" @@@@@@@@@@@        topic   = " + td.get_name() + " (type: " + td.get_type_name() + ")");
//  	System.out.println(" @@@@@@@@@@@        change  = " + status.get_alive_count_change());
//  	System.out.println(" @@@@@@@@@@@        current = " + status.get_alive_count());
// 	System.out.println(" @@@        participant = " + td.get_participant().toString());
 
//  	System.out.println(" @@@@@@@@@@@                             @@@@@@@@@@" );
//  	System.out.println(" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"); 
  	
       }

       @Override
  	public void on_subscription_matched(DataReader dr, 
  					  SubscriptionMatchedStatus status)
       { 
  	TopicDescription   td = dr.get_topicdescription();
  	System.out.println(" @@@@@@@@@@@     SUBSCRIPTION MATCHED    @@@@@@@@@@"); 
  	System.out.println(" @@@@@@@@@@@        topic   = " + td.get_name() + " (type: " + td.get_type_name() + ")");
  	System.out.println(" @@@@@@@@@@@        current = " + status.get_current_count());
  	System.out.println(" @@@@@@@@@@@                             @@@@@@@@@@" );
  	System.out.println(" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"); 
       }

       @Override
  	public void on_sample_lost(DataReader dr, 
  				 SampleLostStatus status) { 
    	   System.out.println(" @@@@@@@@@@@   SAMPLE LOST    @@@@@@@@@@"); 
       };

       @Override
    public void on_data_available(DataReader dr)
    {	
       TopicDescription td = dr.get_topicdescription();
       dataDDSDataReader data_message = (dataDDSDataReader) dr;
       System.out.println(" @@@@@@@@@@@     DATA AVAILABLE          @@@@@@@@@@"); 
   	   System.out.println(" @@@@@@@@@@@        topic = " + td.get_name() + " (type: " + td.get_type_name() + ")");
   	
       samples = new dataDDSSeq();
       SampleInfoSeq si      = new SampleInfoSeq();
       
       ReturnCode_t  retval  = data_message.take(samples, si, 100, 
		       coredx.DDS_ANY_SAMPLE_STATE, 
		       coredx.DDS_ANY_VIEW_STATE, 
		       coredx.DDS_ANY_INSTANCE_STATE);
       System.out.println(" @@@@@@@@@@@        DR.read() ===> " + retval);
  	   
       if (retval == ReturnCode_t.RETCODE_OK)
 	  {
 	    if (samples.value == null)
 	      System.out.println(" @@@@@@@@@@@        samples.value = null");
 	    else
 	      {
 		System.out.println(" @@@@@@@@@@@        samples.value.length= " + samples.value.length);
 		for (int i = 0; i < samples.value.length; i++)
 		  {
 		    System.out.println("    State       : " + 
 				       (si.value[i].instance_state == 
 					coredx.DDS_ALIVE_INSTANCE_STATE?"ALIVE":"NOT ALIVE") );
 		    System.out.println("    TimeStamp   : " + si.value[i].source_timestamp.sec + "." + 
                                                               si.value[i].source_timestamp.nanosec);
 		    System.out.println("    Handle      : " + si.value[i].instance_handle.value);
 		    System.out.println("    WriterHandle: " + si.value[i].publication_handle.value);
 		    System.out.println("    SampleRank  : " + si.value[i].sample_rank);
 		    if (si.value[i].valid_data)
 		    System.out.println("       XVel: " + samples.value[i].XVel_DDS);
 		    System.out.println("       YVel: " + samples.value[i].YVel_DDS);
 		    System.out.println(" CompassDir: " + samples.value[i].CompassDir_DDS);
 		    System.out.println("     GPS_LT: " + samples.value[i].GPS_LT_DDS);
 		    System.out.println("     GPS_LN: " + samples.value[i].GPS_LN_DDS);
 		  }
 	      }
 	    data_message.return_loan(samples, si);
 	  }
 	else
 	  {
 	  }
 	System.out.println(" @@@@@@@@@@@                             @@@@@@@@@@" );
 	System.out.println(" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"); 
       };
     };

     System.out.println("STARTING -------------------------");
     DomainParticipantFactory dpf = DomainParticipantFactory.get_instance();
     dpf.set_license(license);
	 dpf.get_default_participant_qos(dp_qos_tablet);
     DomainParticipant dp = null;

     System.out.println("CREATE PARTICIPANT ---------------");
     dp = dpf.create_participant(0,    /* domain Id   */
    		 dp_qos_tablet,
    		 //null, /* default qos */
  				null, /* no listener */
  				0);

     
     //Tablet variable - create DDS entities for reading tablet data
   //   dp = dpf.create_participant(0, dp_qos_tablet, null, 0);
     
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
     
    SubscriberQos sub_qos_tablet = new SubscriberQos();
 	Log.i("Tablet","creating publisher/subscriber");
 	sub_tablet = dp.create_subscriber(sub_qos_tablet, null, 0);
     
     System.out.println("REGISTERING TYPE -----------------");
     //LongDynamicType data = new LongDynamicType();
    
     /*
     tablet_type = new StructDynamicType();
 	 tablet_type.set_num_fields(5);
 	 
 	 tablet_type.set_field(0, "XVel_DDS",new LongDynamicType(), false);
     tablet_type.set_field(1, "YVel_DDS", new LongDynamicType(), false);
     tablet_type.set_field(2, "CompassDir_DDS", new LongDynamicType(), false);
     tablet_type.set_field(3, "GPS_LN_DDS", new LongDynamicType(), false);
     tablet_type.set_field(4, "GPS_LT_DDS", new LongDynamicType(), false);
     tablet_type.set_field(5, "GPS_LT_DDS", new LongDynamicType(), false);
     */
     
	 
	 dataDDSTypeSupport ts = new dataDDSTypeSupport();
 	
     ReturnCode_t returnValue = ts.register_type(dp, null);
 	
 	if(returnValue != ReturnCode_t.RETCODE_OK)
 	{
 	  System.out.println("ERROR registering type\n");
 	  return;
 	 }
 	
 	System.out.println("Declare TS -----------------");
	    
	//TypeSupport ts = DynamicType.create_typesupport(tablet_type);
	 //TypeSupport ts = StringDynamicType.create_typesupport(data);
	 //ReturnCode_t retval = ts.register_type(dp, ts.get_type_name());
 	

    
    System.out.println("TS Declared -----------------");
    //if (dp == null) return; // failed to initialize DDS, nothing to draw...


        	  
     System.out.println("CREATE TOPIC ---------------------");
  //   Topic              top          = dp.create_topic("helloTopic", ts.get_type_name(), 
  //  		 DDS.TOPIC_QOS_DEFAULT, // default qos
  //						      null, 0); // no listener
  
	  /* create a DDS Topic with the FilterMsg data type. */
	Topic topics= dp.create_topic("dataDDS",ts.get_type_name(), 
   		DDS.TOPIC_QOS_DEFAULT,
   		null,
   		0);
	
	if(topics == null)
	{
		System.out.println("Error creating topic");
		return;
	}
	
     System.out.println("CREATE SUBSCRIBER ----------------");
     SubscriberQos       sub_qos      = null;
     SubscriberListener  sub_listener = null;
     Subscriber          sub          = dp.create_subscriber(sub_qos, sub_listener, 0);  

     System.out.println("READER VARIABLES ----------------");
     DataReaderQos dr_qos = new DataReaderQos();
     sub.get_default_datareader_qos(dr_qos);
     dr_qos.entity_name.value = "JAVA_DR";
     dr_qos.history.depth = 10;
     DataReaderListener dr_listener = new TestDataReaderListener();
     
     System.out.println("CREATE DATAREADER ----------------");
     
     //DynamicTypeDataReader dr = (DynamicTypeDataReader) sub.create_datareader(top, dr_qos, dr_listener, coredx.getDDS_ALL_STATUS());
     
     //Create DDS Data reader
	 dataDDSDataReader dr= (dataDDSDataReader) sub.create_datareader(topics, 
             DDS.DATAREADER_QOS_DEFAULT,
             dr_listener, 
             DDS.DATA_AVAILABLE_STATUS);
   
     System.out.println("DATAREADER CREATED ----------------");
     
     //Cheack to see if DDS Data Reader worked 
	    if(dr == null)
     {
       System.out.println("ERROR creating data reader\n");
       //return;
      }
     
     while ( true ) {
    	 
       try {
  
  	Thread.currentThread().sleep(1000);   // 5 second sleep
       } catch (Exception e) {
  	e.printStackTrace();
       }
     
           
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_dds__subscriber);
     }
      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dds__subscriber, menu);
        return true;
    }
};
        
