package net.MLPHUB.teamMLH;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import net.MLPHUB.teamMLH.R;

public class About extends Activity{
	
	SharedPreferences prefs;
	Editor editor;
	private static String URL_EQC,URL_WEB;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mlh_main_about);
		
		Typeface fontMain = Typeface.createFromAsset(getAssets(),
				"fonts/fontmain.TTF");
		TextView name = (TextView) findViewById(R.id.about);
		name.setTypeface(fontMain);
		
		prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);
		int back = prefs.getInt("background", R.drawable.mane6);
		
		getWindow().setBackgroundDrawableResource(back);
		URL_EQC  = getString(R.string.urlBaseEQC);
		URL_WEB = getString(R.string.urlWEB);
		
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			TextView appversion = (TextView)findViewById(R.id.app_version);
			appversion.setText("My Little Hub v."+pInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	public void btnWeb(View v){
		Intent browserAction = new Intent(Intent.ACTION_VIEW, Uri
				.parse(URL_WEB));
		startActivity(browserAction);
	}
	
	public void btnEQC(View v){
		
		Intent browserAction = new Intent(Intent.ACTION_VIEW, Uri
				.parse(URL_EQC));
		startActivity(browserAction);
		
	}
public void btnCtUs(View v){
		
		Intent action = new Intent(About.this, Feedback.class);
		startActivity(action);
		
	}

}
