package net.MLPHUB.teamMLH.Options;

import net.MLPHUB.teamMLH.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class Preferences extends Activity{
	
	private Intent optionI;
	private SharedPreferences prefs;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mlh_options_preferences);
		prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);
		int back = prefs.getInt("background", R.drawable.mane6);
		getWindow().setBackgroundDrawableResource(back);
		
		Typeface app = Typeface.createFromAsset(getAssets(),
				"fonts/SEGOEUIL.TTF");
		TextView options = (TextView)findViewById(R.id.options);
		Button setFeed = (Button) findViewById(R.id.btnSetFeed);
		Button backgroundOP = (Button) findViewById(R.id.btnBackgrounds);
		options.setTypeface(app);
		setFeed.setTypeface(app);
		backgroundOP.setTypeface(app);
	}
	
	public void btnSetFeed(View v)
	{
		optionI = new Intent(Preferences.this, SetRss.class);
		startActivity(optionI);
	}
	
	public void btnBackgrounds(View v)
	{
		optionI = new Intent(Preferences.this, Backgrounds.class);
		startActivity(optionI);
	}
	
	public void onResume()
	{
		super.onResume();
		int back = prefs.getInt("background", R.drawable.mane6);
 		getWindow().setBackgroundDrawableResource(back);
	}

}
