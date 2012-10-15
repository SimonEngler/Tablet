package com.example.zoetablet;





import com.toc.coredx.DDS.DDS;
import com.toc.coredx.DDS.DataReaderQos;
import com.toc.coredx.DDS.DataWriterQos;
import com.toc.coredx.DDS.DomainParticipant;
import com.toc.coredx.DDS.DomainParticipantFactory;
import com.toc.coredx.DDS.DomainParticipantQos;
import com.toc.coredx.DDS.DynamicType;
import com.toc.coredx.DDS.DynamicTypeDataReader;
import com.toc.coredx.DDS.DynamicTypeDataWriter;
import com.toc.coredx.DDS.Locator;
import com.toc.coredx.DDS.LocatorKind;
import com.toc.coredx.DDS.LongDynamicType;
import com.toc.coredx.DDS.ParticipantLocator;
import com.toc.coredx.DDS.Publisher;
import com.toc.coredx.DDS.PublisherQos;
import com.toc.coredx.DDS.StringDynamicType;
import com.toc.coredx.DDS.StructDynamicType;
import com.toc.coredx.DDS.Subscriber;
import com.toc.coredx.DDS.Topic;
import com.toc.coredx.DDS.TypeSupport;

import android.content.Context;
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

//Shapes Imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.SocketException;

//Android Imports
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;

//Import Java Classes
import java.util.Random;
import java.text.DecimalFormat;
import java.util.Vector;

//Zoe Tablet imports


@SuppressLint("ParserError")
public class BasicFragmentActivity extends FragmentActivity {
	
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
	  public double XVel;
	  public double YVel;
	  public double CompassDir;
	  public double GPS_LN;
	  public double GPS_LT;
	  DatalogFragment datalog; 
	  Random generator = new Random();
	  
	  //View variables Tablet
	  public static TextView tv_datalogFragment = null;
	  public static TextView tv_XVel = null;
	  public static TextView tv_YVel = null;
	  public static TextView tv_CompassDir = null;
	  public static TextView tv_GPS_LT = null;
	  public static TextView tv_GPS_LN = null;
	  public static TextView tv_GPSLocation = null;
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
        mcastLock = wifi.createMulticastLock("TocShapes");
        mcastLock.acquire();

       // shapes = new Vector<Writer>();
        
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
    	  Log.e("Tablet", e.getMessage());
    	}
          }
        license = new String(license + ">");
        Log.i("Tablet", "...License seems to be good");
        
   
         
        //Initialize Variables
        XVel = 1.0;
  	    YVel = 2.0;
  	    CompassDir = 3.0;
  	    GPS_LN = 4.0;
  	    GPS_LT = 5.0;
/*
  	    tabletVariables.XVel_DDS = XVel;
  	    tabletVariables.YVel_DDS = YVel;
  	    tabletVariables.CompassDir_DDS = CompassDir;
  	    tabletVariables.GPS_LN_DDS = GPS_LN;
  	    tabletVariables.GPS_LT_DDS = GPS_LT;
  */	    
  	     
        dpf_tablet = DomainParticipantFactory.get_instance(); // get DomainParticipantFactory
        dpf_tablet.set_license(license);                      // configure DPF with the license string
        dpf_tablet.get_default_participant_qos(dp_qos_tablet);
        
        //*************************
        //Tablet variable - create DDS entities for reading tablet data
        dp_tablet = dpf_tablet.create_participant(0, dp_qos_tablet, null, 0);
        
        if(dp_tablet == null)
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
        else
        {
        	PublisherQos pub_qos_tablet = new PublisherQos();
        	Log.i("Tablet","creating publisher/subscriber");
        	
        	sub_tablet = dp_tablet.create_subscriber(null, null, 0);
        	dp_tablet.get_default_publisher_qos(pub_qos_tablet);
        	pub_tablet = dp_tablet.create_publisher(pub_qos_tablet, null, 0);
        	
        	DataReaderQos dr_qos_tablet = new DataReaderQos();
        	DataWriterQos dw_qos_tablet = new DataWriterQos();
        	
        	sub_tablet.get_default_datareader_qos(dr_qos_tablet);
        	pub_tablet.get_default_datawriter_qos(dw_qos_tablet);
        	
        	// Construct a DynamicType based on the 'Tablet' data type
        	//  -- This is an alternative to using a TypeSupport 
        	//  generated via the coredx_ddl tool.
        	Log.i("Tablet", "creating 'Tablet' data type...");
        	tablet_type = new StructDynamicType();
        	tablet_type.set_num_fields(5);
        	StringDynamicType Label = new StringDynamicType();
        	Label.set_max_length(128);
        	
        	 tablet_type.set_field(0, "XVel_DDS",new LongDynamicType(), false);
	         tablet_type.set_field(1, "YVel_DDS", new LongDynamicType(), false);
	         tablet_type.set_field(2, "CompassDir_DDS", new LongDynamicType(), false);
	         tablet_type.set_field(3, "GPS_LN_DDS", new LongDynamicType(), false);
	         tablet_type.set_field(4, "GPS_LT_DDS", new LongDynamicType(), false);
	         tablet_type.set_field(5, "GPS_LT_DDS", new LongDynamicType(), false);
	        	
        	TypeSupport ts = DynamicType.create_typesupport(tablet_type);
        	ts.register_type(dp_tablet, "TabletType");
        	
        	for (int i = 0;i < topic_names_tablet.length; i++) 
      	  {
            Log.i("Tablet", "creating " + topic_names_tablet[i] + " Topic...");
      	    topics_tablet[i] = dp_tablet.create_topic(topic_names_tablet[i], "TabletType", DDS.TOPIC_QOS_DEFAULT, null, 0);
      	    Log.i("Tablet", "creating " + topic_names_tablet[i] + " Reader...");
      	    readers_tablet[i] = (DynamicTypeDataReader)sub_tablet.create_datareader(topics_tablet[i], dr_qos_tablet, null, 0);
      	    Log.i("Tablet", "creating " + topic_names[i] + " Writer...");
      	    writers_tablet[i] = (DynamicTypeDataWriter)pub_tablet.create_datawriter(topics_tablet[i], dw_qos_tablet, null, 0);
      	  }
            }
          Log.i("Tablet", "...finished initialize Tablet DDS");
        	
          //Broadcast Tablet Data
          Log.i("Tablet", "Begin Publish Data");
       
          /*
          newTabletWriter(writers_tablet,tabletVariables.XVel_DDS
        		  ,tabletVariables.YVel_DDS
        		  ,tabletVariables.CompassDir_DDS
        		  ,tabletVariables.GPS_LT_DDS
        		  ,tabletVariables.GPS_LN_DDS);
          */
        // newTabletWriter(writers_tablet,XVel,YVel,CompassDir,GPS_LT,GPS_LN);
        newTabletWriter();
         
        
 
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
            
            // We alter the state of Fragments in the FragmentManager using a FragmentTransaction. 
            // FragmentTransaction's have access to a Fragment back stack that is very similar to the Activity
            // back stack in your app's task. If you add a FragmentTransaction to the back stack, a user 
            // can use the back button to undo a transaction. We will cover that topic in more depth in
            // the second part of the tutorial.
        	
            
        	//Shapes Fragment
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

   		public void OnMoved(int pan, int tilt) {
   			txtX1.setText(Integer.toString(pan));
   			txtY1.setText(Integer.toString(tilt));
   		}

   		public void OnReleased() {
   			txtX1.setText("released");
   			txtY1.setText("released");
   		}
   		
   		public void OnReturnedToCenter() {
   			txtX1.setText("stopped");
   			txtY1.setText("stopped");
   		};
   	}; 

       JoystickMovedListener _listenerRight = new JoystickMovedListener() {

   		public void OnMoved(int pan, int tilt) {
   			txtX2.setText(Integer.toString(pan));
   			txtY2.setText(Integer.toString(tilt));
   		}

   		public void OnReleased() {
   			txtX2.setText("released");
   			txtY2.setText("released");
   		}
   		
   		public void OnReturnedToCenter() {
   			txtX2.setText("stopped");
   			txtY2.setText("stopped");
   		};
   	}; 
   	
    joystick.setOnJostickMovedListener(_listenerLeft, _listenerRight);

       
      //Compass Activities
        compassView = (CompassView)this.findViewById(R.id.compassView);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
  	    //updateOrientation(new float[] {0, 0, 0});
        //updateOrientation(calculateOrientation());
  	    

      
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
   
     mHandler.postDelayed(mUpdateDatalog, 50); // every 1 sec */
     
     
     
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

        return values;
      }
      
      //Compass Function
      private final SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
          if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            aValues = event.values;
          if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mValues = event.values;
       

       //   updateOrientation(calculateOrientation());
          
         
        }

        //Compass Function
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
     	};
     	
     	@Override
     	protected void onResume() {
     		
     		  Log.i("Tablet","Resume...");
     	
     	  //Compass Activity 
     	  Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
     	  Sensor magField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
     	  sensorManager.registerListener(sensorEventListener, 
     	                                 accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
     	  sensorManager.registerListener(sensorEventListener, 
     	                                 magField,
     	                                 SensorManager.SENSOR_DELAY_FASTEST);   
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
     	      public void onClick(View v) {
     		finish();
     	      }
     	    };
     	    
     	    
          //runnable to periodically update our IP address on main screen 
     	  private Runnable mUpdateMyAddress = new Runnable() {
     	      public void run() {
     		BasicFragmentActivity.updateMyAddress();

     		// and do it again, later
     		mHandler.postDelayed(mUpdateMyAddress, 20000);
     	      }
     	    };
     	    
     	    //Update datalog
     	    private Runnable mUpdateDatalog = new Runnable() {
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

     	  
     	
//     	 public static void newTabletWriter(double XVel,double YVel,double CompassDir,
//				  double GPS_LN, double GPS_LT)
    
     	  public  void newTabletWriter()
     	 {
     	     //BasicFragmentActivity Variables = null;	 
     		 for (int i = 0; i < 3; i++)
    	      {
    		if (writers_tablet.equals(topic_names_tablet[i]))
    		  {
    		    TabletWriter wTablet = new TabletWriter (writers_tablet[i],XVel,YVel,CompassDir,
  					  GPS_LN, GPS_LT);
    			
    		    // TabletWriter wTablet = new TabletWriter (writers_tablet[i],XVel,YVel,CompassDir,
    			//		  GPS_LN, GPS_LT);
    					  
    		    		
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
     	        String oldInfo = null;
     	        
     	       Log.i("Tablet", "...In update function");
          	      if (BasicFragmentActivity.tablet != null)
          		{
          	    	Log.i("Tablet", "...there is a tablet activity");
          		}
                    Log.i("Tablet","ITERATOR: " + BasicFragmentActivity.writers_tablet.length);
                    for(int a=0; a < (BasicFragmentActivity.writers_tablet.length); a++)
                    		{
                    	       Log.i("Tablet","values: " + writers_tablet[a].toString());
                    		}
          		    // tabletWriter = BasicFragmentActivity.tablet.elementAt(1);
          		//  for (; iterator.hasNext(); )
          		//   {
          		      
          		    //  TabletWriter tabletWriter = iterator.next();
                    if(tabletWriter.isEmpty())
                    {
                    	 oldInfo = getDatalogValues(XVel);
                    }
                    else
                    {
                    	 oldInfo = getDatalogValues(tabletWriter.elements().nextElement().XVel_DDS);
                    	
                    }
          		 //     Xvelocity = oldInfo.toCharArray();
         	     //     tv_XVel.setText(Xvelocity,0,Xvelocity.length);

          	    //     Log.i("Tablet", "...In update tabler iterator. XVel= " + tabletWriter.XVel_DDS);
          		 //   }
          	//	}
          	//    Log.i("Tablet", "...passed update function");
     	  
     	        //X velocity
      	        oldInfo = getDatalogValues(XVel);
     	        Xvelocity = oldInfo.toCharArray();
     	        tv_XVel.setText(Xvelocity,0,Xvelocity.length); 
          
     	        //Y velocity
     	        oldInfo = getDatalogValues(YVel);
    	        Yvelocity = oldInfo.toCharArray();
    	        tv_YVel.setText(Yvelocity,0,Yvelocity.length); 
     	        
     	        //GPS Location
    	        oldInfo = getDatalogValues(GPS_LN) + "  W  " + getDatalogValues(GPS_LT) + "  N";
     	        GPS = oldInfo.toCharArray();
     	        tv_GPSLocation.setText(GPS,0,GPS.length);
     	        
     	        //Compass Direction
    	        oldInfo = getDatalogValues(CompassDir);
     	        Compass = oldInfo.toCharArray();
     	        tv_CompassDir.setText(Compass,0,Compass.length);
     	        
     	         mHandler.postDelayed(mUpdateDatalog, 1000); // every 1 sec */
     	        
     	    }

     	};
     	
