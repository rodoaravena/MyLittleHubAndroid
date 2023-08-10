package net.MLPHUB.teamMLH.Options;



import java.util.Random;

import net.MLPHUB.teamMLH.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Backgrounds extends Activity{
	
	private SharedPreferences prefs;
	private Editor editor;
	private int back;
	private boolean changed = false;
	
	/**Views*/
	private TextView txt;
    private Button back01, back02, back03, back04, back05, back06, back07, back08, cambio;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.mlh_options_backgrounds);
        
        /**Set Views*/
        txt = (TextView)findViewById(R.id.txt);
        back01 = (Button)findViewById(R.id.btn_back01);
        back02 = (Button)findViewById(R.id.btn_back02);
        back03 = (Button)findViewById(R.id.btn_back03);
        back04 = (Button)findViewById(R.id.btn_back04);
        back05 = (Button)findViewById(R.id.btn_back05);
        back06 = (Button)findViewById(R.id.btn_back06);
        back07 = (Button)findViewById(R.id.btn_back07);
        back08 = (Button)findViewById(R.id.btn_back08);
        cambio = (Button)findViewById(R.id.cambio);
        
        prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);
		back = prefs.getInt("background", R.drawable.mane6);
        
        Typeface app = Typeface.createFromAsset(getAssets(),
				"fonts/SEGOEUIL.TTF");
        
        txt.setTypeface(app);
        txt.setText(R.string.background);
        txt.setSelected(true);
        editor=prefs.edit();
    }
	
	public void onResume(){
 		super.onResume();
 		int back = prefs.getInt("background", R.drawable.mane6);
 		getWindow().setBackgroundDrawableResource(back);
 	}
	
	public void setWallpaperApp(int bg){
		
		
		switch (bg)
	    {
	    	case 2:
	    		editor.putInt("background", R.drawable.mane6);
	    		break;
	    	case 3:
	    		
	    		editor.putInt("background", R.drawable.ts);
	    		
	    		break;
	    	case 4:
	    		
	    		 editor.putInt("background", R.drawable.rr);
	    		break;
	    	case 5:
	    		
	    		 editor.putInt("background", R.drawable.rd);
	    		break;
	    	case 6:
	    		
	    		 editor.putInt("background", R.drawable.fs);
	    		 	    		 
	    		break;
	    	case 7:
	    		editor.putInt("background", R.drawable.aj);
	    		
	    		break;
	    		    	
	    	case 8:
	    		editor.putInt("background", R.drawable.pp);
	    		break;
	    	default:
	    		editor.putInt("background", R.drawable.mane6);
	    		break;
	    		
	    }
		
		editor.commit();
	
		
		
		back = prefs.getInt("background", R.drawable.mane6);
		getWindow().setBackgroundDrawableResource(back);
		
		
	
	}
	
	public void setWallpaperRadio(int bg)
	{
		switch (bg)
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
	    	
	    }
		editor.commit();
		back = prefs.getInt("backgroundRadio", R.drawable.mane6);
		getWindow().setBackgroundDrawableResource(back);
		
	}
	
	
	/**Selección de botones**/
	
	public void back01(View v){
		 editor.putBoolean("backgroundRandom", true);
		 
		 Random r = new Random();
		    int bgApp = r.nextInt(9-2);
		    
		    setWallpaperApp(bgApp);
		// editor.commit();
		
		 //setWallpaper();
	     //finish();
	}
	public void back02(View v){
		editor.putBoolean("backgroundRandom", false);
		 setWallpaperApp(2);
	    // finish();
	}
	public void back03(View v){
		
	
		
		if(!changed)
		{
			editor.putBoolean("backgroundRandom", false);
		 setWallpaperApp(3);
		}
		else if(changed)
		{
			 Random r = new Random();
			    int bgRadio = r.nextInt(3-0);
			editor.putBoolean("backgroundRadioRandom", true);
			setWallpaperRadio(bgRadio);
			//editor.commit();
		}
		
	}
	public void back04(View v){
		
		if(!changed)
		{
			editor.putBoolean("backgroundRandom", false);
		 setWallpaperApp(4);
		}
		else if(changed)
		{
			editor.putBoolean("backgroundRadioRandom",false);
			setWallpaperRadio(0);
		}
		
	}
	public void back05(View v){
		if(!changed)
		{
			editor.putBoolean("backgroundRandom", false);
			setWallpaperApp(5);
		}
		else if(changed)
		{
			editor.putBoolean("backgroundRadioRandom",false);
			setWallpaperRadio(1);
		}
		
	}
	public void back06(View v){
		
		if(!changed)
		{	
			editor.putBoolean("backgroundRandom", false);
			setWallpaperApp(6);
		}
		else if(changed)
		{
			editor.putBoolean("backgroundRadioRandom",false);
			setWallpaperRadio(2);
		}
	}
	public void back07(View v){
	
		editor.putBoolean("backgroundRandom", false);
		 setWallpaperApp(7);
	}
	
	public void back08(View v){
		editor.putBoolean("backgroundRandom", false);
		 setWallpaperApp(8);
	}
	
	
	
	
	public void change(View v)
	{
		System.out.println("CLICK! changed = "+ changed);
		if(!changed)
		{
			getWindow().setBackgroundDrawableResource(prefs.getInt("backgroundRadio", R.drawable._back));
			
			txt.setText(R.string.backgroundRadio);
			cambio.setText(R.string.background);
			back01.setBackgroundDrawable(null);
			back01.setText(null);
			back01.setEnabled(false);
			back02.setBackgroundDrawable(null);
			back02.setText(null);
			back02.setEnabled(false);
			back03.setBackgroundDrawable(getResources().getDrawable(R.drawable.random));
			back03.setText(R.string.random);
			back04.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_def));
			back04.setText(R.string.def);
			back05.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_vs));
			back05.setText(R.string.vs);
			back06.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_tavi));
			back06.setText(R.string.ot);
			back07.setBackgroundDrawable(null);
			back07.setText(null);
			back07.setEnabled(false);
			back08.setBackgroundDrawable(null);
			back08.setText(null);
			back08.setEnabled(false);
			changed = true;
		}
		else if(changed)
		{
			getWindow().setBackgroundDrawableResource(prefs.getInt("background", R.drawable.mane6));
			txt.setText(R.string.background);
			cambio.setText(R.string.backgroundRadio);
			
			back01.setBackgroundDrawable(getResources().getDrawable(R.drawable.random));
			back01.setText(R.string.random);
			back01.setEnabled(true);
			
			back02.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_mane6));
			back02.setText(R.string.m6);
			back02.setEnabled(true);
			
			back03.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_ts));
			back03.setText(R.string.ts);
			
			back04.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_rr));
			back04.setText(R.string.rr);
			
			back05.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_rd));
			back05.setText(R.string.rd);
			
			back06.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_fs));
			back06.setText(R.string.fs);
			
			back07.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_aj));
			back07.setText(R.string.aj);
			back07.setEnabled(true);
			
			back08.setBackgroundDrawable(getResources().getDrawable(R.drawable.thumb_pp));
			back08.setText(R.string.pp);
			back08.setEnabled(true);
			changed = false;
		}
		
		
	}

}
