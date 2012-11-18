//Zoe Android Robot Interface (ZARI)
//
//Copyright (C) 2012 Carnegie Mellon University (CMU)
//All Rights Reserved.
//
//Permission to use, copy, modify and distribute this software and its
//documentation is hereby granted, provided that both the copyright notice and
//this permission notice appear in all copies of the software, derivative works
//or modified versions, and any portions thereof, and that both notices appear
//in supporting documentation.
//
//Carnegie Mellon University ALLOWS FREE USE OF THIS SOFTWARE IN ITS "AS IS" CONDITION.
//Carnegie Mellon University DISCLAIMS ANY LIABILITY OF ANY KIND FOR ANY DAMAGES
//WHATSOEVER RESULTING FROM THE USE OF THIS SOFTWARE.

//Any question, please contact the author:
//	Simon Engler
//	Sensor Networks Laboratory
//	Department of Electrical and Computer Engineering
//	University of Calgary
//	email: stengler@ucalgary.ca
//	       simon.engler@gmail.com

package com.example.zoetablet;

import java.util.HashMap;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.toc.coredx.DDS.DDS;
import com.toc.coredx.DDS.DynamicTypeSeq;
import com.toc.coredx.DDS.LongDynamicType;
import com.toc.coredx.DDS.ReturnCode_t;
import com.toc.coredx.DDS.SampleInfoSeq;
import com.toc.coredx.DDS.StringDynamicType;
import com.toc.coredx.DDS.StructDynamicType;

import com.example.zoetablet.R;
import com.example.zoetablet.BasicFragmentActivity;


public class ShapesViewFragment extends SurfaceView 
  implements SurfaceHolder.Callback 
{

  private final class HandlerExtension extends Handler {
		@Override
		public void handleMessage(Message m) { }
	}

public class ShapesThread extends Thread 
  {
    /** Indicate whether the surface has been created & is ready to draw */
    private boolean mSurface = false;

    /** Handle to the surface manager object we interact with */
    private SurfaceHolder mSurfaceHolder;
        
    public static final int STATE_START    = -1;
    public static final int STATE_RUNNING  = 1;
    public static final int STATE_PAUSE    = 2;
    public static final int STATE_STOP     = 3;
    public static final int STATE_DESTROY  = 4;
        
    private int mState;
    //private int mCanvasHeight = 1;
    //private int mCanvasWidth = 1;
        
    private HashMap<String, Integer> colorMap;
    private Paint                    paint;
    private Path                     mPath = null;
    private BitmapDrawable           mLogo = null;

    
    public ShapesThread(SurfaceHolder surfaceHolder, 
                        Context       context, 
                        Handler       handler) 
    {
	

      // create graphic resources
      mState         = STATE_START;
      mSurfaceHolder = surfaceHolder;
      paint    = new Paint();            
      colorMap = new HashMap<String, Integer>(); 
            
      colorMap.put("black", Color.BLACK);
      colorMap.put("red", Color.RED);
      colorMap.put("green", Color.GREEN);
      colorMap.put("blue", Color.BLUE);
      colorMap.put("cyan", Color.CYAN);
      colorMap.put("yellow", Color.YELLOW);
      colorMap.put("orange", Color.rgb(255,165,0));
      colorMap.put("magenta", Color.MAGENTA);            
      colorMap.put("BLACK", Color.BLACK);
      colorMap.put("RED", Color.RED);
      colorMap.put("GREEN", Color.GREEN);
      colorMap.put("BLUE", Color.BLUE);
      colorMap.put("CYAN", Color.CYAN);
      colorMap.put("YELLOW", Color.YELLOW);
      colorMap.put("ORANGE", Color.rgb(255,165,0));
      colorMap.put("MAGENTA", Color.MAGENTA);            
      colorMap.put("Black", Color.BLACK);
      colorMap.put("Red", Color.RED);
      colorMap.put("Green", Color.GREEN);
      colorMap.put("Blue", Color.BLUE);
      colorMap.put("Cyan", Color.CYAN);
      colorMap.put("Yellow", Color.YELLOW);
      colorMap.put("Orange", Color.rgb(255,165,0));
      colorMap.put("Magenta", Color.MAGENTA);            

      mPath = new Path();
      Resources res = getResources();
      mLogo = (BitmapDrawable)res.getDrawable(R.drawable.coredx_logo_240x270);
      mLogo.setBounds(0, 0, 240, 270);
            
      android.util.Log.i("Shapes", "display thread intialized");

    }
		
    /** indicate that the surface is valid */
    public void setSurface(boolean b) 
    {
    	 android.util.Log.i("Shapes", "set surface");
      synchronized (mSurfaceHolder) {
	mSurface = b;
      }
    }

    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
      // synchronized to make sure these all change atomically
      synchronized (mSurfaceHolder) {
	// Note: these values are not used, currently
    	  android.util.Log.i("Shapes", "set surface");
	//mCanvasWidth  = width;
	//mCanvasHeight = height;
      }
    }
        
    private void doDraw(Canvas canvas) {
    	 android.util.Log.i("Shapes", "draw canvas");
      canvas.clipRect(0,  0, 240, 270);
      
      // clear the canvas
      //paint.setColor(Color.WHITE);
      //canvas.drawRect(0,0,240,270, paint);
  
      // draw background
      mLogo.draw(canvas);

      if (BasicFragmentActivity.dp == null) return; // failed to initialize DDS, nothing to draw...

      // draw Rx'd shapes
      for (int t = 0; t < BasicFragmentActivity.topic_names.length; t++) {
        // read Shapes:
        DynamicTypeSeq  samples = new DynamicTypeSeq();
        SampleInfoSeq si      = new SampleInfoSeq();
        ReturnCode_t  retval  = BasicFragmentActivity.readers[t].read(samples, si, 
						       DDS.LENGTH_UNLIMITED, 
						       DDS.ANY_SAMPLE_STATE, 
						       DDS.ANY_VIEW_STATE, 
						       DDS.ALIVE_INSTANCE_STATE);
        if (retval == ReturnCode_t.RETCODE_OK) {
          StructDynamicType shape;
          StringDynamicType f_color;
          LongDynamicType   f_x;
          LongDynamicType   f_y;
          LongDynamicType   f_size;
          int count = samples.value.length;
          for (int i = 0;i < count; i++) {
            if (si.value[i].sample_rank == 0) {
              shape = (StructDynamicType)samples.value[i];
              f_color = (StringDynamicType)shape.get_field(0);
              f_x     = (LongDynamicType)shape.get_field(1);
              f_y     = (LongDynamicType)shape.get_field(2);
              f_size  = (LongDynamicType)shape.get_field(3);
                    		
                    		
              paint.setColor(colorMap.get(f_color.get_string()));
                    		
              int size = f_size.get_long();
              int x    = f_x.get_long();
              int y    = f_y.get_long();
                    		
              if (t == 0) // square
                {
                  x -= size/2;
                  y -= size/2;
                  // draw square
                  mPath.addRect(x,y,x+size,y+size, Path.Direction.CW);
                }
              else if (t == 1)  // circle 
                {
                  mPath.addCircle(x,y, size/2, Path.Direction.CW);
                }
              else // triangle
                {
                  y -= size/2;
                  mPath.moveTo(x,y);
                  mPath.lineTo(x+size/2, y+size);
                  mPath.lineTo(x-size/2,y+size);
                  mPath.close();
                }
              canvas.drawPath(mPath,paint);
              mPath.reset();
            }
          }
          BasicFragmentActivity.readers[t].return_loan(samples, si);
        }
      }
    }
        
    public void setState(int state) {
      synchronized (mSurfaceHolder) {
        // change state if needed
        if (mState != state) {
          mState = state;
        }
      }
    }
        
    @Override
	public void run() 
    {
      android.util.Log.i("Shapes", "display thread starting...");
      while (mState != STATE_DESTROY) {
     Canvas c = null;

	if ((mState == STATE_RUNNING) && (mSurface)) {
	  try {
	    c = mSurfaceHolder.lockCanvas(null);
	   
	    synchronized (mSurfaceHolder) {
	      doDraw(c);
	    }
	  } finally {
	    // do this in a finally so that if an exception is thrown
	    // during the above, we don't leave the Surface in an
	    // inconsistent state
	    if (c != null) {
	      mSurfaceHolder.unlockCanvasAndPost(c);

	    }
	  }// end finally block
	  android.util.Log.i("Shapes", "9");
	  // move and publish our 'local' shapes 
	 // BasicFragmentActivity.publishLocalShapes();
	 // BasicFragmentActivity.publishTabletData();

	  // sleep a bit --> 20 updates / sec
	  try { Thread.sleep(50); } catch (Exception e) {}

	}
	else {
	  try { Thread.sleep(100); } catch (Exception e) {}
	}
      }// end while !DESTROY block
      android.util.Log.i("Shapes", "display thread done...");
    }		
   };
	
	
  private ShapesThread thread;
    
  /**
   * The constructor called from the main activity
   * 
   * @param context 
   * @param attrs 
   */
  public ShapesViewFragment(Context context, AttributeSet attrs) {
    super(context, attrs);
    Log.i("Shapes", "ShapesView() constructor");
    // register our interest in hearing about changes to our surface
    SurfaceHolder holder = getHolder();
    Log.i("Shapes", "ShapesView() constructor again");
    holder.addCallback(this);
    Log.i("Shapes", "ShapesView() constructor this");
    // create thread unless in layout editor.
    if (isInEditMode() == false) {
    	  Log.i("Shapes", "ShapesView() constructor edit");
      thread = new ShapesThread(holder, context, new HandlerExtension());
      thread.start();
      Log.i("Shapes", "ShapesView() constructor started");
    }
  }
	
  public ShapesThread getThread() {
    return thread;
  }
	    
  /* Callback invoked when the surface dimensions change. */
  //	@Override
  @Override
public void 
  surfaceChanged(SurfaceHolder holder, 
                 int format, 
                 int width, 
                 int height) 
  {
    thread.setSurfaceSize(width, height);
  }

  //	@Override
  @Override
public void surfaceCreated(SurfaceHolder arg0) {
    // start the thread here so that we don't busy-wait in run()
    // waiting for the surface to be created
    Log.i("Shapes", "surfaceCreated()...");
    thread.setSurface(true);
  }

  //	@Override    
  @Override
public void surfaceDestroyed(SurfaceHolder arg0) {
    Log.i("Shapes", "surfaceDestroyed()...");
    thread.setSurface(false);
  }
}
