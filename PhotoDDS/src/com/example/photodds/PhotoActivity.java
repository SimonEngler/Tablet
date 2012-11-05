package com.example.photodds;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;


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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//DDS Tablet Data Imports
import com.example.ddspackage.dataDDS;
import com.example.ddspackage.dataDDSDataReader;
import com.example.ddspackage.dataDDSSeq;
import com.example.ddspackage.dataDDSTypeSupport;
import com.example.ddspackage.dataDDSDataWriter;

import java.nio.Buffer;
import java.nio.channels.FileChannel;


public class PhotoActivity extends Activity {
	
	private static final int ACTION_TAKE_PHOTO_B = 1;
	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageView mImageView;
	private Bitmap mImageBitmap;
	private String mCurrentPhotoPath;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;    
	
	
	public static StructDynamicType        tablet_type   = null;
    public static DynamicTypeDataWriter    writers_tablet      = null;
    public static DynamicTypeDataWriter[]  writers = null;
    public static DomainParticipantQos     dp_qos_tablet       = new DomainParticipantQos();
    public static Publisher                pub_tablet          = null;
    public static PublisherQos             pub_qos_tablet      = null;
    public static PublisherListener 	   pub_listener_tablet = null;
    public static Topic	                   topics              = null;
    public static MulticastLock            mcastLock = null;
  
    
	  //Declare DDS Variables
		dataDDS dataMessage;
		dataDDSDataReader dataDataReader;
		dataDDSDataWriter dataWriter;
		dataDDSSeq dataSeq;
		dataDDSTypeSupport dataTypeSup;
		dataDDSDataWriter dw;
		ReturnCode_t returnValue;
		Random generator = new Random();
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    byte[] buffer = null;
	    
	    
	    
	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	
	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		return f;
	}

	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	   	ImageView image = (ImageView) findViewById(R.id.imageView1);
	   	image.setImageBitmap(bitmap);
		
		/* Associate the Bitmap to the ImageView */
		mImageView.setVisibility(View.VISIBLE);
	}

	private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
			File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    this.sendBroadcast(mediaScanIntent);
	}

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO_B:
			
			File f = null;
			
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;                                                                                                                                                                                                                                                     
				mCurrentPhotoPath = null;
			}
			
			break;

		default:
			break;			
		} // switch
		startActivityForResult(takePictureIntent, actionCode);
	}

	private void handleBigCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			 setPic();
			galleryAddPic();
			mCurrentPhotoPath = null;
		}
	
	}
  


	private void handleCameraVideo(Intent intent) {
		mImageBitmap = null;
		mImageView.setVisibility(View.INVISIBLE);
	}

	Button.OnClickListener mTakePicOnClickListener = 
		new Button.OnClickListener() {
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
		}
	};


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		   setContentView(R.layout.activity_photo);
		   
			
		    //Set up Image Capture buttons  
			mImageBitmap = null;
			Button picBtn = (Button) findViewById(R.id.btnIntend);
			setBtnListenerOrDisable( 
					picBtn, 
					mTakePicOnClickListener,
					MediaStore.ACTION_IMAGE_CAPTURE
			);
					System.out.println( "setting up button done");
	
					
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
						mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
					} else {
						mAlbumStorageDirFactory = new BaseAlbumDirFactory();
					}		
					
		  
		//Enable WiFi Communication
   	 WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
         mcastLock = wifi.createMulticastLock("dataDDS");
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
       ReturnCode_t returnValue = ts.register_type(dp,null);
   	   
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
	    System.out.println("CREATE DATAWRITER ----------------");
	    DataWriterListener dw_listener = null;
 	
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
	    System.out.println("READING IMAGE FILE ----------------");
	    String fileName = "/storage/sdcard0/DCIM/Camera/robot.jpg";
        
	   // File fnew = new File("/storage/sdcard0/DCIM/Camera/robot.jpg");
	    Bitmap bmp; 
	   
	    bmp = BitmapFactory.decodeFile(fileName);
	    ByteArrayOutputStream baos=new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	    buffer = baos.toByteArray();
	    
	    System.out.println("Image Loaded ----------------");
	    System.out.println("Buffer is: " + buffer.length);
	    System.out.println("BOAS is: " + baos.size());
	  
	    System.out.println("WRITE MESSAGE TO DDS ----------------");
	    	     
		System.out.println("initialize data...\n");
	 	dataDDS dataMessage = new dataDDS();
 		dataMessage.XVel_DDS = getDatalogValues(666);
 		dataMessage.YVel_DDS = getDatalogValues(666);
 		dataMessage.CompassDir_DDS = getDatalogValues(124);
 		dataMessage.GPS_LN_DDS = getDatalogValues(12);
 		dataMessage.GPS_LT_DDS = getDatalogValues(13);
 		dataMessage.data_image_DDS = buffer;		
 		System.out.println("data ready...\n");
    	
 	
 		//Send data to DDS stucture
	    while ( true ) {
	    	dataMessage.XVel_DDS = getDatalogValues(666);
	 		dataMessage.YVel_DDS = getDatalogValues(666);
	 		dataMessage.CompassDir_DDS = getDatalogValues(124);
	 		dataMessage.GPS_LN_DDS = getDatalogValues(12);
	 		dataMessage.GPS_LT_DDS = getDatalogValues(13);
	 		dataMessage.data_image_DDS = buffer;		
	 		System.out.println("WRITING DDS MESSAGE...\n");
           returnValue = dw.write(dataMessage, null);

           try {
		       Thread.currentThread();
			Thread.sleep(5000);   // 5 second sleep
	      } catch (Exception e) {
		e.printStackTrace();
	      }
           
	    	//Check to see if message worked
		    if(returnValue != ReturnCode_t.RETCODE_OK )
		    {
		      System.out.println("ERROR writing sample " + returnValue);
			//  return;
			}
		    System.out.println( "DDS_DataWriter_write() " + returnValue);

	    }
		    
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			if (resultCode == RESULT_OK) {
				handleBigCameraPhoto();
			}
			break;
		} // ACTION_TAKE_PHOTO_B
		} // switch
		
	}

	
	private void sendPhotoDDS()
	{
		//Send DDS message via button?
	}
	
	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
				savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? 
						ImageView.VISIBLE : ImageView.INVISIBLE
		);
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void setBtnListenerOrDisable( 
			Button btn, 
			Button.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);        	
		} else {
			btn.setText( 
				getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}
	
	 public float getDatalogValues(float value)
	  {  
		 //Set the value
		 value = value*generator.nextFloat();
       value = Float.valueOf(twoDForm.format(value)); 
		 return value;
	  }
	 
	 public void showStats( String where, FileChannel fc, Buffer b ) throws IOException
     {
        System.out.println( where +
                  " channelPosition: " +
                  fc.position() +
                  " bufferPosition: " +
                  b.position() +
                  " limit: " +
                  b.limit() +
                  " remaining: " +
                  b.remaining() +
                  " capacity: " +
                  b.capacity() );
     }
	 
	 
	

}