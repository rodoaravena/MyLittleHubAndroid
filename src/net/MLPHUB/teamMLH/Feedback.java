package net.MLPHUB.teamMLH;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class Feedback extends Activity
{
	private EditText bodyMail;
	private Intent sendMail;
	private String manufacturer = Build.MANUFACTURER;
	private String model = Build.MODEL;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mlh_main_feedback);
		
		Typeface fontMain = Typeface.createFromAsset(getAssets(),
				"fonts/fontmain.TTF");
		Typeface fontSegoe = Typeface.createFromAsset(getAssets(),
				"fonts/SEGOEUIL.TTF");
		TextView appName = (TextView)findViewById(R.id.appname);
		TextView name = (TextView) findViewById(R.id.contact);
		name.setTypeface(fontSegoe);
		appName.setTypeface(fontMain);
		
		bodyMail = (EditText)findViewById(R.id.bodyMail);
		
		sendMail = new Intent(Intent.ACTION_SEND);
		sendMail.setType("message/rfc822");
		sendMail.putExtra(Intent.EXTRA_EMAIL, new String[]{"foxglm@gmail.com"});
		sendMail.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.subjectMail));	
		
		
	}
	
	public void btnSend(View v)
	{
		String body = bodyMail.getText().toString() + "\n" +
					  getString(R.string.manufacture)+ " " + manufacturer + "\n"+
					  getString(R.string.modelo)+ " " + model;
					  
		
		sendMail.putExtra(Intent.EXTRA_TEXT, body);
		
		startActivity(Intent.createChooser(sendMail, "Select email application."));
		finish();
		
	}

}
