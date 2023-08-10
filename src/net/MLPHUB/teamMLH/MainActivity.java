package net.MLPHUB.teamMLH;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import net.MLPHUB.teamMLH.Episodes.EpisodeList;
import net.MLPHUB.teamMLH.Feeds.MenuNews;
import net.MLPHUB.teamMLH.Options.Preferences;
import net.MLPHUB.teamMLH.Options.SetRss;
import net.MLPHUB.teamMLH.Radio.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.BannerView;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.ReceivedBannerInterface;

import net.MLPHUB.teamMLH.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;

public class MainActivity extends Activity
{
	
	 private SharedPreferences prefs, rss;
	 private Editor editor;
	 private DownloadTask task = new DownloadTask();
	 private static String URL_RSS;
	 private int index=0, fT, fC, fA, tam, time = 0;
	 private Intent loadActivity;
	 private final String TAG = "MainActivity";
	 private Context context;
	 private ViewGroup.MarginLayoutParams params;
	 private RelativeLayout myRelativeLayout;
	 private FrameLayout frame;
	 private Handler handler = new Handler();
	 private Timer timer = new Timer();
	 private BannerView mBanner;
	 
	
	 
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mlh_main_tablet_main_activity);
		
		context = getApplicationContext();
		
		mBanner = new BannerView(context);
		myRelativeLayout = (RelativeLayout) findViewById(R.id.MLHBannerView);
		//frame = (FrameLayout) findViewById(R.id.MLHbannerFragment);
		myRelativeLayout.addView(mBanner, new LayoutParams(LayoutParams.MATCH_PARENT, 70));
		
		mBanner.getAdSettings().setPublisherId(923875680);
		mBanner.getAdSettings().setAdspaceId(65823837);
		mBanner.asyncLoadNewBanner();
		
		params = (ViewGroup.MarginLayoutParams) myRelativeLayout.getLayoutParams();
		
		tam = params.height;
		
		
		
		TimerTask timerBanner = new TimerTask(){
		      
		      public void run() 
		      {
		          handler.post(new Runnable()
		          {
		              public void run() 
		              {
		            	  if(time>2000)
		            	  {
		            		  Log.v(TAG, "timer banner tam = " + tam);
			            	  params.height = tam;
			            	  //myRelativeLayout.setLayoutParams(params);
			            	  tam--;
			            	  if(tam < -10)
			            	  {
			            		  myRelativeLayout.setVisibility(View.GONE);
			            		  if(tam == -17)
			            		  {
			            			  timer.cancel();
			            		  }
			            		  
			            	  }
		            	  }
		            	  Log.v(TAG, "Current time: " + time);
		            	  time++;
		            	         	  
		              };
		          });		          
		      }
		  };
		
		timer.schedule(timerBanner, 100,10);	
		
		Typeface fontMain = Typeface.createFromAsset(getAssets(),
				"fonts/fontmain.TTF");
		Typeface app = Typeface.createFromAsset(getAssets(),
				"fonts/SEGOEUIL.TTF");
		
		TextView name = (TextView) findViewById(R.id.textTitle);
		TextView start = (TextView) findViewById(R.id.textStart);
		
		start.setTypeface(app);
		name.setTypeface(fontMain);
		name.setSelected(true);
		name.setText(R.string.app_name);
		
		prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);
		int back = prefs.getInt("background", R.drawable.mane6);
		
		URL_RSS = getString(R.string.urlFeeds);
		rss = getSharedPreferences("Fuentes",Context.MODE_PRIVATE);
		editor=rss.edit();
		
		
		
		
		getWindow().setBackgroundDrawableResource(back);
			
		Button btnFeed = (Button) findViewById(R.id.btnFeed);
		btnFeed.setTypeface(app);
		btnFeed.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				System.out.println("intent on click");
				if(fC==0||fT!=fA)
				{
					System.out.println("fC = "+fC+"fT = "+fT+"fA = "+fA);
					loadActivity = new Intent(MainActivity.this, SetRss.class);
					loadActivity.putExtra("Configurado", false);
					
				}
				else if(fC!=0)
				{
					loadActivity = new Intent(MainActivity.this, MenuNews.class);
				}
				
				startActivity(loadActivity);
			}
		});
		
		Button btnEpList = (Button) findViewById(R.id.btnEpList);
		btnEpList.setTypeface(app);
		btnEpList.setOnClickListener(new OnClickListener() {
			public void onClick(View arg1) {
				loadActivity = new Intent(MainActivity.this, EpisodeList.class);
				startActivity(loadActivity);
			}
		});
		
		Button btnEFRRadio = (Button) findViewById(R.id.btnERad);
		btnEFRRadio.setTypeface(app);
		btnEFRRadio.setOnClickListener(new OnClickListener() {
			public void onClick(View arg2) {
				loadActivity = new Intent(MainActivity.this, Everfree.class);
				startActivity(loadActivity);
			}
		});
		
		Button btnSRBRadio = (Button) findViewById(R.id.btnSRad);
		btnSRBRadio.setTypeface(app);
		btnSRBRadio.setOnClickListener(new OnClickListener() {
			public void onClick(View arg2) {
				loadActivity = new Intent(MainActivity.this, SonicRadioBoom.class);
				startActivity(loadActivity);
			}
		});
		
		launchDownloadFeeds();
	}

	public boolean onCreateOptionsMenu(Menu m) {
		getMenuInflater().inflate(R.menu.mainmenu, m);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.about:
			Intent loadAbout = new Intent(MainActivity.this, About.class);
			startActivity(loadAbout);
			break;
		case R.id.settings:
			Intent loadSet = new Intent(MainActivity.this,Preferences.class);
			startActivity(loadSet);			
			break;
		}
		return true;
	}

 
 	public void onResume(){
 		super.onResume();
 		int back = prefs.getInt("background", R.drawable.mane6);
 		getWindow().setBackgroundDrawableResource(back);
 		
 		fT = rss.getInt("fuentesTotales", 0);
		fC = rss.getInt("fuentesConfiguradas", 0);
		fA = rss.getInt("fuentesActuales", 0);
 	}
 	
 	
 //--------------------------------Descarga fuentes background---------------------------------
 	
 	void launchDownloadFeeds() {
		try {
			
			task.execute(new URL(URL_RSS));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
 	
 	public class DownloadTask extends AsyncTask<URL, String, String> {


		boolean error = false;

		@Override
		protected String doInBackground(URL... params) {
			try {
				Log.v(TAG, "Conectando...");
				URL url = params[0];
				XmlPullParserFactory parserCreator = XmlPullParserFactory
						.newInstance();
				XmlPullParser parser = parserCreator.newPullParser();
				parser.setInput(url.openStream(), null);
				int parserEvent = parser.getEventType();

				while (parserEvent != XmlPullParser.END_DOCUMENT) {
					
					switch (parserEvent) {
					case XmlPullParser.START_TAG:
						String tag = parser.getName();
						
						if (tag.equalsIgnoreCase("RSSFeed")) {
							
							Log.v(TAG, "Deserializando RSSFeed, por favor espere...");
							
							String name =  parser.getAttributeValue(null,
									"Name");
									editor.putString("Name"+index,name);
							//editor.commit();
							//System.out.println("Name"+index+": "+rss.getString("Name"+index, "notfound"));
							
							editor.putString("FeedUrl"+name, parser.getAttributeValue(null,
									"FeedUrl"));
							//editor.commit();
							//System.out.println("FeedUrl"+name+": "+rss.getString("FeedUrl"+name, "notfound"));
							
							editor.putString("Type"+name, parser.getAttributeValue(null,
									"Type"));
							//editor.commit();
							
							//System.out.println("Type"+name+": "+rss.getString("Type"+name, "notfound"));
													
							editor.putString("WebUrl"+name, parser.getAttributeValue(null,
									"WebUrl"));
							//editor.commit();
							//System.out.println("WebUrl"+name+": "+rss.getString("WebUrl"+name, "notfound"));
							
							editor.putString("ShortName"+name, parser.getAttributeValue(null,
									"ShortName"));
							//editor.commit();
							//System.out.println("ShortName"+name+": "+rss.getString("ShortName"+name, "notfound"));
							
							editor.putString("Hashtag"+name, parser.getAttributeValue(null,
									"Hashtag"));
							//editor.commit();
							//System.out.println("Hashtag"+name+": "+rss.getString("Hashtag"+name, "notfound"));
							
							//System.out.println("Show"+name+": "+rss.getBoolean("Show"+name, false));
							editor.commit();
							
							Log.i(TAG, "Fuente almacenada: "
									  +"\nNombre: "+rss.getString("Name"+index, "notfound")
									  +"\nFeedURL: "+rss.getString("FeedUrl"+name, "notfound")
									  +"\nTipo: "+rss.getString("Type"+name, "notfound")
									  +"\nWebURL: "+rss.getString("WebUrl"+name, "notfound")
									  +"\nNombre corto: "+rss.getString("ShortName"+name, "notfound")
									  +"\nHashtag: "+rss.getString("Hashtag"+name, "notfound")
									  +"\n¿Está activo?: "+rss.getBoolean("Show"+name, false)
								  );
							
							boolean itemClosed = false;
							editor.commit();
							index++;
							parserEvent = parser.getEventType();

							while (parserEvent != XmlPullParser.END_DOCUMENT
									&& !itemClosed) 
							{
								switch (parserEvent) 
								{
									case XmlPullParser.END_TAG:

										tag = parser.getName();
										if (tag.equalsIgnoreCase("RSSFeed")) 
										{
											itemClosed = true;
										}
									break; 
								}
								parserEvent = parser.next();
							}
						
						}
						break;
					}
					parserEvent = parser.next();
					
					Log.v(TAG, "Ha terminado la descarga de fuentes disponibles");
					
				}
				
				parserEvent = parser.next();
				editor.putInt("fuentesTotales", index);
				editor.commit();
				Log.v(TAG, "fuentesTotales: "+rss.getInt("fuentesTotales", 0));
				Log.v(TAG, "fuentesConfiguradas: "+rss.getInt("fuentesConfiguradas", 0));
			} 

			catch (Exception e) {
				Log.e(TAG + " Error", "Ocurrió un error: ", e);
				error = true;
			}

			return null;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (error) 
			{
				Log.e(TAG, "Finalizó la deserialización con errores");
				Log.w(TAG, "Es posible que no se hayan almacenado algunas fuentes.");
			}
			else
			{
				Log.i(TAG, "Se han guardado todas las fuentes disponibles satisfactoriamente!");	
			}
			
					
		}
		
		

	}
 	


}
