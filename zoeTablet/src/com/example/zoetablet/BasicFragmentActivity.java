package com.example.zoetablet;

//Import DDS Library
import com.toc.coredx.DDS.DDS;
import com.toc.coredx.DDS.DataReader;
import com.toc.coredx.DDS.DataReaderListener;
import com.toc.coredx.DDS.DataReaderQos;
import com.toc.coredx.DDS.DomainParticipant;
import com.toc.coredx.DDS.DomainParticipantFactory;
import com.toc.coredx.DDS.DomainParticipantQos;
import com.toc.coredx.DDS.DynamicTypeDataReader;
import com.toc.coredx.DDS.DynamicTypeDataWriter;
import com.toc.coredx.DDS.LivelinessChangedStatus;
import com.toc.coredx.DDS.Locator;
import com.toc.coredx.DDS.LocatorKind;
import com.toc.coredx.DDS.ParticipantLocator;
import com.toc.coredx.DDS.Publisher;
import com.toc.coredx.DDS.RequestedDeadlineMissedStatus;
import com.toc.coredx.DDS.RequestedIncompatibleQosStatus;
import com.toc.coredx.DDS.ReturnCode_t;
import com.toc.coredx.DDS.SampleInfoSeq;
import com.toc.coredx.DDS.SampleLostStatus;
import com.toc.coredx.DDS.SampleRejectedStatus;
import com.toc.coredx.DDS.StructDynamicType;
import com.toc.coredx.DDS.Subscriber;
import com.toc.coredx.DDS.SubscriberListener;
import com.toc.coredx.DDS.SubscriberQos;
import com.toc.coredx.DDS.SubscriptionMatchedStatus;
import com.toc.coredx.DDS.Topic;
import com.toc.coredx.DDS.TopicDescription;
import com.toc.coredx.DDS.coredxConstants;

//Import Android library
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;

//Java Imports
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Vector;


//DDS Tablet Data Imports
import com.example.ddspackage.dataDDS;
import com.example.ddspackage.dataDDSDataReader;
import com.example.ddspackage.dataDDSSeq;
import com.example.ddspackage.dataDDSTypeSupport;
import com.example.ddspackage.dataDDSDataWriter;


@SuppressLint("ParserError")
public class BasicFragmentActivity extends FragmentActivity {
		
    dataDDS dataMessage;
    dataDDSDataReader dataDataReader;
    dataDDSDataWriter dataWriter;
    dataDDSSeq dataSeq;
 	dataDDSSeq samples;
    dataDDSTypeSupport dataTypeSup;
    ReturnCode_t returnValue;
    public byte[] buffer;
    public float compassV0;
    public float compassV1;
    public float compassV2;
    
	//Joystick variables
	TextView txtX1, txtY1;
	TextView txtX2, txtY2;
	DualJoystickView joystick;
	
    
	//Compass variables
	float[] aValues = new float[3];
	float[] mValues = new float[3];
	
	CompassView compassView;
	SensorManager sensorManager;
	ShapesViewFragment shapesView;
    
      //Shapes Variables
	  public static DomainParticipantFactory dpf          = null;
	  public static DomainParticipant        dp           = null;
	  public static DomainParticipantQos     dp_qos       = new DomainParticipantQos();
	  public static Subscriber               sub          = null;
	  public static Publisher                pub          = null;
	  public static String[]                 topic_names  = { "Square", "Circle", "Triangle" };
	  public static Topic[]                  topics       = { null, null, null };
	  public static StructDynamicType        shape_type   = null;
      public static DynamicTypeDataReader[]  readers      = { null, null, null };
	  public static DynamicTypeDataWriter[]  writers      = { null, null, null };
      public static Vector<Writer>           shapes = null; /* shapes we are publishing */
	  public static MulticastLock            mcastLock = null;
	  public static TextView                 tv_myAddr = null;
	 

	  /** A handle to the thread that's actually running the animation. */
	  //private ShapesThread mShapesThread;
	  private Handler      mHandler = new Handler();

	  /** A handle to the View in which the animation is running. */
	  //private ShapesViewFragment mShapesView;

	  protected final int ADD_WRITER_CODE=0;
	  protected final int DEL_WRITER_CODE=1;
	  protected final int ADD_PEER_CODE  =3;
	  
	  //Tablet Variables
	  public static Vector<TabletWriter> tablet = null; /* tablet data we are publishing */
	  public static DomainParticipantFactory dpf_tablet          = null;
	  public static DomainParticipant        dp_tablet           = null;
	  public static DomainParticipantQos     dp_qos_tablet       = new DomainParticipantQos();
	  public static Subscriber               sub_tablet          = null;
	  public static Publisher                pub_tablet          = null;
	  public static String[]                 topic_names_tablet  = { "Compass", "LogTerm", "Video" };
	  public static Topic[]                  topics_tablet       = { null, null, null };
	  public static StructDynamicType        tablet_type   = null;
      public static DynamicTypeDataReader[]  readers_tablet      = { null, null, null };
	  public static DynamicTypeDataWriter[]  writers_tablet      = { null, null, null };
      public static Vector<TabletWriter>     tablet_data = null; /* shapes we are publishing */
	  public static MulticastLock            mcastLock_tablet = null;
	  public static TextView                 tv_myAddr_tablet = null;
	  
	  //Tablet Data
	  public static Vector<TabletWriter> tabletData = null; /* Tablet data we are publishing */
	  public static float XVel;
	  public static float YVel;
	  public static float CompassDir;
	  public static float GPS_LN;
	  public static float GPS_LT;
	  public static String Log_DDS;
	  public static byte[] data_image_DDS;
	  DatalogFragment datalog; 
	  Random generator = new Random();
	  BasicFragment pictureData;
	  
	  //View variables Tablet
	  public static TextView tv_datalogFragment = null;
	  public static TextView tv_XVel = null;
	  public static TextView tv_YVel = null;
	  public static TextView tv_CompassDir = null;
	  public static TextView tv_GPS_LT = null;
	  public static TextView tv_GPS_LN = null;
	  public static TextView tv_GPSLocation = null;
	  public static TextView tv_Log_DDS = null;
	  public static TextView tv_data_image_DDS = null;
	  public static TextView tv_Log_enter = null;

	  //Set the decimal format
      DecimalFormat twoDForm = new DecimalFormat("#.##");
      TabletWriter tabletVariables = null;
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //Set the orientation
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    
        //Shapes and Coredx DDS Initialization
        // magic to enable WiFi multicast to work on some android platforms:
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        mcastLock = wifi.createMulticastLock("Tablet");
        mcastLock.acquire();

        //Tablet variables
        tablet = new Vector<TabletWriter>();
              
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
    	//  Log.e("Tablet", e.getMessage());
    	}
          }
        license = new String(license + ">");
        Log.i("Tablet", "...License seems to be good");
        
        //Initialize Variables
        XVel = 1;
  	    YVel = 2;
  	    CompassDir = 3;
  	    GPS_LN = 4;
  	    GPS_LT = 5;
  	    Log_DDS = DateFormat.getDateTimeInstance().format(new Date());
  	    
  	    //For the Image, use current image display and 
  	    String fileName = "/storage/sdcard0/DCIM/no_video.jpg";
	    Bitmap bmp; 
	    bmp = BitmapFactory.decodeFile(fileName);
	    ByteArrayOutputStream baos=new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	    buffer = baos.toByteArray();
  	    data_image_DDS = buffer;  
  	    
  		 Log.i("Tablet", "Creating Subscriber");
  	     class TestDataReaderListener implements DataReaderListener 
  	     {
  	    	
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
  	      	TopicDescription   td = dr.get_topicdescription();
  	    	System.out.println(" @@@@@@@@@@@     LIVELINESS CHANGED  " +  td.get_name() +  " @@@@@@@@@@"); 
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
  			       coredxConstants.DDS_ANY_SAMPLE_STATE, 
  			       coredxConstants.DDS_ANY_VIEW_STATE, 
  			       coredxConstants.DDS_ANY_INSTANCE_STATE);
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
  	 					coredxConstants.DDS_ALIVE_INSTANCE_STATE?"ALIVE":"NOT ALIVE") );
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
  	 		    System.out.println("     Log_DDS: " + samples.value[i].Log_DDS);
  	 	
  	 		    //Capture data values for display
  	 		    BasicFragmentActivity.XVel =  samples.value[i].XVel_DDS;
  	 		    BasicFragmentActivity.YVel =  samples.value[i].YVel_DDS;
  	 		    BasicFragmentActivity.CompassDir =  samples.value[i].CompassDir_DDS;
  	 		    BasicFragmentActivity.GPS_LT =  samples.value[i].GPS_LT_DDS;
  	 		    BasicFragmentActivity.GPS_LN =  samples.value[i].GPS_LN_DDS;
  	 		    BasicFragmentActivity.Log_DDS = samples.value[i].Log_DDS;
  	 		    BasicFragmentActivity.data_image_DDS = samples.value[i].data_image_DDS;
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
       
       if(dp == null)
       {
       	//failed to create DomainParticipant -- bad license
       	android.util.Log.e("CoreDX DDS", "Unable to create Tablet DomainParticipant.");
       }
       
      SubscriberQos sub_qos_tablet = new SubscriberQos();
   	Log.i("Tablet","creating publisher/subscriber");
   	sub_tablet = dp.create_subscriber(sub_qos_tablet, null, 0);
       
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
       
             
        // We default to building our Fragment at runtime, but you can switch the layout here
        // to R.layout.activity_fragment_xml in order to have the Fragment added during the
        // Activity's layout inflation.
        
        setContentView(R.layout.holygrail);
        Log.i("Tablet", "called set content view");
        
        FragmentManager fm       = getSupportFragmentManager();
        Fragment        fragment = fm.findFragmentById(R.id.center_pane_top); // You can find Fragments just like you would with a 
                                                                         // View by using FragmentManager.
     
        
        Log.i("Tablet", "...declare fragment");
        
        // If wLog.ie are using activity_fragment_xml.xml then this the fragment will not be
        // null, otherwise it will be.
       if (fragment == null) {
            
        
        	//Image View Fragment
            Log.i("Debug", "...calling fragment center pane");
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.center_pane_top, new BasicFragment());
            ft.addToBackStack(null);

            //Logging Fragment
            Log.i("Debug", "...calling fragment left pane top");
            //ft.add(R.id.left_pane_top, new LoggingFragment());
            ft.add(R.id.left_pane_top, new LoggingFragment());
            ft.addToBackStack(null);

            //Connecting Fragment
            Log.i("Debug", "...calling fragment left pane bottom");
            //ft.add(R.id.left_pane_bottom,new ControllerFragment());
            ft.add(R.id.left_pane_bottom, new ConnectionFragment());
            ft.addToBackStack(null);
            
            //Compass Fragment
            Log.i("Debug", "...calling fragment right pane top");
            //ft.add(R.id.right_pane_top,new BasicFragment2());
            ft.add(R.id.right_pane_top, new BasicFragment2());
            ft.addToBackStack(null);
            
            //Navigation Fragment
            Log.i("Debug", "...calling fragment right pane bottom");
            //ft.add(R.id.center_pane_bottom,new NavigationFragment());
            ft.add(R.id.center_pane_bottom, new DualJoystickActivity());
            ft.addToBackStack(null);
            
            //Datalog Fragment
            Log.i("Debug", "...calling fragment middle pane bottom");
            //ft.add(R.id.right_pane_bottom,new DatalogFragment());
            ft.add(R.id.right_pane_bottom, new DatalogFragment());
            ft.addToBackStack(null);
            
            //Commit the fragment or it will not be added
            Log.i("Debug", "...comitting");
            ft.commit();                 
        }
       
        //Joystick variables
   	   txtX1 = (TextView)this.findViewById(R.id.TextViewX1);
       txtY1 = (TextView)this.findViewById(R.id.TextViewY1);
   	   txtX2 = (TextView)this.findViewById(R.id.TextViewX2);
       txtY2 = (TextView)this.findViewById(R.id.TextViewY2); 
       joystick = (DualJoystickView)this.findViewById(R.id.dualjoystickView);
       
       JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

   		@Override
		public void OnMoved(int pan, int tilt) {
   			txtX1.setText(Integer.toString(pan));
   			txtY1.setText(Integer.toString(tilt));
   		}

   		@Override
		public void OnReleased() {
   			txtX1.setText("released");
   			txtY1.setText("released");
   		}
   		
   		@Override
		public void OnReturnedToCenter() {
   			txtX1.setText("stopped");
   			txtY1.setText("stopped");
   		};
   	}; 

       JoystickMovedListener _listenerRight = new JoystickMovedListener() {

   		@Override
		public void OnMoved(int pan, int tilt) {
   			txtX2.setText(Integer.toString(pan));
   			txtY2.setText(Integer.toString(tilt));
   		}

   		@Override
		public void OnReleased() {
   			txtX2.setText("released");
   			txtY2.setText("released");
   		}
   		
   		@Override
		public void OnReturnedToCenter() {
   			txtX2.setText("stopped");
   			txtY2.setText("stopped");
   		};
   	}; 
   	
    joystick.setOnJostickMovedListener(_listenerLeft, _listenerRight);

       
      //Compass Activities
        compassView = (CompassView)this.findViewById(R.id.compassView);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
  	  //  updateOrientation(new float[] {0, 0, 0});
      //  updateOrientation(calculateOrientation());
  	    

      
      // Hook up button presses to the appropriate event handler.
      // Quit Button -- not really necessary: The 'Back' button does 
      //   the same thing, but we create this just for fun..
      ((Button) findViewById(R.id.quitButton)).setOnClickListener(mQuitListener);
      
      tv_myAddr = (TextView)findViewById(R.id.myAddress);
      if (tv_myAddr != null)
        tv_myAddr.setText("<detecting>");
      mHandler.postDelayed(mUpdateMyAddress, 1000); // every 10 sec */
        
     //Updating Datalog fragment view
     tv_XVel = (TextView)findViewById(R.id.XVel);
     if (tv_XVel != null)
         tv_XVel.setText("<detecting>");
   
     
     tv_YVel = (TextView)findViewById(R.id.YVel);
     if (tv_YVel != null)
         tv_YVel.setText("<detecting>");
   
     tv_CompassDir = (TextView)findViewById(R.id.CompassDir);
     if (tv_CompassDir != null)
         tv_CompassDir.setText("<detecting>");
   
     
     tv_GPSLocation = (TextView)findViewById(R.id.GPSLocation);
     if (tv_GPSLocation != null)
         tv_GPSLocation.setText("<detecting>");
   
      tv_Log_DDS = (TextView) findViewById(R.id.loggingMessage);
    		 if(tv_Log_DDS != null)
    			 tv_Log_DDS.setText("<detecting>");
      
     mHandler.postDelayed(mUpdateDatalog, 500); // every 1 sec */
    }
    

    //Compass function
    private void updateOrientation(float[] values) {
        if (compassView!= null) {
            compassView.setBearing(values[0]);
            compassView.setPitch(values[1]);
     	    compassView.setRoll(-values[2]);
     	    compassView.invalidate();
     	  }
     	}
    
      //Compass function
      public float[] calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        float[] outR = new float[9];

        SensorManager.getRotationMatrix(R,null, aValues, mValues);
        SensorManager.remapCoordinateSystem(R, 
                                            SensorManager.AXIS_X, 
                                            SensorManager.AXIS_Y, 
                                            outR);

        SensorManager.getOrientation(outR, values);

        // Convert from Radians to Degrees.
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);
        
       // System.out.println("Compass " + values[0] + " " + values[1] + " " + values[2]);
        if(values[0] != 0.0 && values[1] != 0.0)
        {
        	compassV0 = values[0];
        	compassV1 = values[1];
        	compassV2 = values[2];
        }
        
        if(values[0] == 0.0 && values[1] == 0.0)
        {
        	//If data is spitting out zeros, keep last good value
        	values[0] =compassV0 ;
        	values[1] =compassV1;
        	values[2] =compassV2;
        }
        return values; 
      }
      
      //Compass Function
      private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
		public void onSensorChanged(SensorEvent event) {
          if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            aValues = event.values;
          if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mValues = event.values;
       
         // updateOrientation(new float[] {0, 0, 0});
          updateOrientation(calculateOrientation());
          
         
        }

        //Compass Function
        @Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
     	};
     	
     	@Override
     	protected void onResume() {
     		
     		  Log.i("Tablet","Resume...");
     	
     	  //Compass Activity 
     	  Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
     	  Sensor magField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
     	  sensorManager.registerListener(sensorEventListener, 
     	                                 accelerometer,SensorManager.SENSOR_DELAY_UI);
     	  sensorManager.registerListener(sensorEventListener, 
     	                                 magField,
     	                                 SensorManager.SENSOR_DELAY_UI);   
     	
           super.onResume();
     	}

     	//Compass Function
     	@Override
     	protected void onStop() {
     		Log.i("Tablet","Stop...");
     	  //Compass Activity
     	  sensorManager.unregisterListener(sensorEventListener);
     	  super.onStop();
     	}
     	
     	//A call-back for when the user presses the 'Quit' button.
     	   
     	  OnClickListener mQuitListener = new OnClickListener() {
     	      @Override
			public void onClick(View v) {
     		finish();
     	      }
     	    };
     	    
     	    
          //runnable to periodically update our IP address on main screen 
     	  private Runnable mUpdateMyAddress = new Runnable() {
     	      @Override
			public void run() {
     		BasicFragmentActivity.updateMyAddress();

     		// and do it again, later
     		mHandler.postDelayed(mUpdateMyAddress, 20000);
     	      }
     	    };
     	    
     	    //Update datalog
     	    private Runnable mUpdateDatalog = new Runnable() {
     	    	@Override
				public void run(){
     	    	updateDatalog(tablet);
     	    	}
     	    };
     	    
     	    
     	   /** hook up a 'menu' to the 'menu' button: */
     	   @Override
     	   public boolean onCreateOptionsMenu(Menu menu) {
     	       MenuInflater inflater = getMenuInflater();
     	       inflater.inflate(R.menu.menu, menu);
     	       return true;
     	   }


     	   
     	  /** called when activity starts */
     	  @Override
     	  public void onStart() {
     	    Log.i("Tablet","Start...");
     	    super.onStart();
     	  }
     	  
     	 /** called when activity pauses */
     	  @Override
     	  public void onPause() {
     	    Log.i("Tablet","Pause...");
     	    super.onPause();
     	  }
     	  
     	  /** called when configuration changes (orientation, screenSize etc) */
    	  @Override
     	  public void onConfigurationChanged(Configuration newConfig) {
     	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     	  }
     	  
     	 /** called when activity is destroyed */
     	  @Override
     	  public void onDestroy() {
     	  //  boolean retry = true;
     	    super.onDestroy();

     	    Log.i("Tablet","Destroy...");

     	    mHandler.removeCallbacks(mUpdateMyAddress);
            mHandler.removeCallbacks(mUpdateDatalog);

     	   
     	    // clean up dds entities
     	 if (dp_tablet != null)
     	 {
      	    Log.i("Tablet","DDS cleaned up...");
     	    dp_tablet.delete_contained_entities();
     	    dpf_tablet.delete_participant(dp_tablet);
     	    mcastLock.release();
     	    mcastLock = null;
     	    Log.i("Tablet","Done..."); 
     	 }
     	  }

     	  
 
     	  public  void newTabletWriter()
     	 {
     	     //BasicFragmentActivity Variables = null;	 
     		 for (int i = 0; i < 3; i++)
    	      {
    		if (writers_tablet.equals(topic_names_tablet[i]))
    		  {
    		    TabletWriter wTablet = new TabletWriter (writers_tablet[i],XVel,YVel,CompassDir,
  					  GPS_LN, GPS_LT,Log_DDS, data_image_DDS);
    			
    		   		  
    		    		
    		    synchronized (tablet) { tablet.add(wTablet); }
    		    break;
    		  }
    	      }
    	  }
     	 
     	
     	  public static void addPeer(String addr, String sidx_start, String sidx_end)
     	  {
     	    int idx_start = Integer.parseInt(sidx_start);
     	    int idx_end   = Integer.parseInt(sidx_end);
     	    
     	    // Configure a few peers we will talk to:
     	    ParticipantLocator pl       = new ParticipantLocator();
     	    pl.participant_locator      = new Locator();
     	    pl.participant_locator.kind = LocatorKind.toInt(LocatorKind.UDPV4_LOCATOR_KIND_QOS);
     	    pl.participant_locator.addr = addr;
     	    pl.participant_id           = idx_start;
     	    pl.participant_id_max       = idx_end;
     	    
     	    //Tablet participant
     	    dp_qos_tablet.peer_participants.value.add(pl);
     	    dp_tablet.set_qos(dp_qos_tablet);
     	  }

     	  /** call to move and publish TabletData  */
     	  public static void publishTabletData()
     	  {
     	    synchronized(BasicFragmentActivity.tablet) {
     	      if (BasicFragmentActivity.tablet != null)
     		{
     		  Iterator<TabletWriter> iter = BasicFragmentActivity.tablet.iterator();
     		  for (; iter.hasNext(); )
     		    {
     		      TabletWriter tabletWriter = iter.next(); 
     		      tabletWriter.updateData();
     		      tabletWriter.publish();
     		    }
     		}
     	    }
     	  }

     	  protected static String getLocalIpAddress() {
     	    try {
     	      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
     		NetworkInterface intf = en.nextElement();
     		for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
     		  InetAddress inetAddress = enumIpAddr.nextElement();
     		  if (!inetAddress.isLoopbackAddress()) {
     		    return inetAddress.getHostAddress().toString();
     		  }
     		}
     	      }
     	    } catch (SocketException ex) {
     	      Log.e("Tablet", ex.toString());
     	    }
     	    return null;
     	  }
     	  
     	 
     	  /** call to update 'myAddress' on main screen */
     	  public static void updateMyAddress()
     	  {
     		 char[] address = null;
     	    String addr = getLocalIpAddress();
     	    
     	    if (tv_myAddr != null)
     	      {
     		if (addr == null)
     		  tv_myAddr.setText("< unknown >");
     		else
     		    address = addr.toCharArray();
     	        tv_myAddr.setText(address,0,address.length);
     			
     	      }
     	  }
     	  
     	 public String getDatalogValues(double value)
    	  {  
    		 //Set the value
    		 value = value*generator.nextDouble();
             value = Double.valueOf(twoDForm.format(value)); 
    		 return Double.toString(value);
    	  }
    	  
     	  
     	    //Data Update function
     	    public void updateDatalog(Vector<TabletWriter> tabletWriter)
     	    {   
     	        char[] Xvelocity = null;
     	        char[] Yvelocity = null;
     	        char[] GPS = null;
     	        char[] Compass = null;
     	        char[] Log_DDS = null;
     	        String oldInfo = null;
     	        
 
          	    Log.i("Tablet", "...passed update function");
     	  
          	    //Log DDS	    
      	        oldInfo = BasicFragmentActivity.Log_DDS + "\n";
     	        Log_DDS = oldInfo.toCharArray();
     	        tv_Log_DDS.setText(Log_DDS, 0, Log_DDS.length);
     	        
     	        //X velocity	    
      	        oldInfo = String.valueOf(BasicFragmentActivity.XVel);
     	        Xvelocity = oldInfo.toCharArray();
     	        tv_XVel.setText(Xvelocity,0,Xvelocity.length); 
     	        //X velocity	    
      	        oldInfo = String.valueOf(BasicFragmentActivity.XVel);
     	        Xvelocity = oldInfo.toCharArray();
     	        tv_XVel.setText(Xvelocity,0,Xvelocity.length); 
          
     	        //Y velocity
     	        oldInfo = String.valueOf(BasicFragmentActivity.YVel);
    	        Yvelocity = oldInfo.toCharArray();
    	        tv_YVel.setText(Yvelocity,0,Yvelocity.length); 
     	        
     	        //GPS Location
    	        oldInfo = getDatalogValues(BasicFragmentActivity.GPS_LN) + "  W  " + getDatalogValues(BasicFragmentActivity.GPS_LT) + "  N";
     	        GPS = oldInfo.toCharArray();
     	        tv_GPSLocation.setText(GPS,0,GPS.length);
     	        
     	        //Compass Direction
    	        oldInfo = getDatalogValues(BasicFragmentActivity.CompassDir);
     	        Compass = oldInfo.toCharArray();
     	        tv_CompassDir.setText(Compass,0,Compass.length);
     	        
     	         mHandler.postDelayed(mUpdateDatalog, 1000); // every 1 sec */
     	        
     	    }

     	};
     	
