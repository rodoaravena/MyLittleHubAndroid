package net.MLPHUB.teamMLH;

import java.util.Random;

import net.MLPHUB.teamMLH.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

public class SplashScreen extends Activity {
	
	protected int _splashTime = 3000; 
	
	private Thread splashTread;
	private SharedPreferences prefs;
	private Editor editor;
	private boolean bgRandomA, bgRandomR;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.mlh_main_splashscreen);
	    
	    Random r = new Random();
	    int bgApp = r.nextInt(7-0);
	    int bgRadio = r.nextInt(3-0);
	    prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);
	    
	    editor=prefs.edit();
	    
	    bgRandomA = prefs.getBoolean("backgroundRandom", false);
	    bgRandomR = prefs.getBoolean("backgroundRadioRandom", false);
	    
	    Log.w("bgA + bgR", bgRandomA+" + "+bgRandomR);
	    
	    Log.v("Random App", ""+bgApp);
	    Log.v("Random Radio", ""+bgRadio);
	    if(bgRandomA)
	    {
		    switch (bgApp)
		    {
		    	case 0:
		    		editor.putInt("background", R.drawable.mane6);
		    		break;
		    	case 1:
		    		editor.putInt("background", R.drawable.ts);
		    		break;
		    	case 2:
		    		editor.putInt("background", R.drawable.rr);
		    		break;
		    	case 3:
		    		 editor.putInt("background", R.drawable.rd);
		    		break;
		    	case 4:
		    		editor.putInt("background", R.drawable.fs);
		    		break;
		    	case 5:
		    		editor.putInt("background", R.drawable.aj);
		    		break;
		    	case 6:
		    		editor.putInt("background", R.drawable.pp);
		    		break;
		    	
		    	default:
		    		editor.putInt("background", R.drawable.mane6);
		    		break;
		    }
	    }
	    if(bgRandomR)
	    {
		    switch (bgRadio)
		    {
		    	case 0:
		    		editor.putInt("backgroundRadio", R.drawable._back);
		    		editor.putBoolean("backgroundRadioDef", true);
		    		break;
		    	case 1:
		    		editor.putInt("backgroundRadio", R.drawable.vs);
		    		editor.putBoolean("backgroundRadioDef", false);
		    		break;
		    	case 2:
		    		editor.putInt("backgroundRadio", R.drawable.ot);
		    		editor.putBoolean("backgroundRadioDef", false);
		    		break;    	
		    	default:
		    		editor.putInt("backgroundRadio", R.drawable._back);
		    		editor.putBoolean("backgroundRadioDef", true);
		    		break;
		    }
	    }
	    
	    editor.commit();
	    
	    final SplashScreen sPlashScreen = this; 
	    
	    // thread for displaying the SplashScreen
	    splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {	            	
	            	synchronized(this){
	            		wait(_splashTime);
	            	}
	            	
	            } catch(InterruptedException e) {} 
	            finally {
	                finish();
	                
	                Intent i = new Intent();
	                i.setClass(sPlashScreen, MainActivity.class);
	        		startActivity(i);
	            }
	        }
	    };
	    
	    splashTread.start();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(splashTread){
	    		splashTread.notifyAll();
	    	}
	    }
	    return true;
	}
	
}
