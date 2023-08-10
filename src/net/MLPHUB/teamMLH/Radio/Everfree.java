package net.MLPHUB.teamMLH.Radio;



import java.io.IOException;
import java.util.Locale;

import net.MLPHUB.teamMLH.MainActivity;

 
import net.MLPHUB.teamMLH.R;
 
import net.MLPHUB.teamMLH.Radio.Service.RadioService;
import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Everfree extends Activity{

	private String TAG = getClass().getSimpleName();
		private SharedPreferences prefs;
		private TextView songName, textState;
		
		private final String EMISORA = "efr";
		
		private String currentSong="", prevSong="", radio = "";
	    private boolean radioIsRun = false, mBufferBroadcastIsRegistered, isOnline, cNotice = false;
	    private Intent radioService;
	    private Button play, share, search, home;
	    private int estado = 0;
	    
	    
	    
	   
	    public void onCreate(Bundle savedInstanceState) {
	    	
	    	
	        super.onCreate(savedInstanceState);
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.mlh_radio_radio);
	        
	        prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);//Recupera las configuraciones de sharedPreferences
	        if(prefs.getBoolean("backgroundRadioDef", true))
	        {
	        	getWindow().setBackgroundDrawableResource(R.drawable.efr_back);
	        }
	        else
	        {
	        	getWindow().setBackgroundDrawableResource(prefs.getInt("backgroundRadio", R.drawable.efr_back));
	        }
	        
	        
	        
	        radioService =new Intent(this, RadioService.class);	
	        
	        Typeface fontMain = Typeface.createFromAsset(getAssets(), "fonts/fontmain.TTF");
	        Typeface fontSegoe = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUIL.TTF");
	        
	 		TextView name = (TextView)findViewById(R.id.titleRadio);  
	 		TextView nameApp = (TextView)findViewById(R.id.appname); 
	 		
	 		play = (Button)findViewById(R.id.playRadio);
	 		share = (Button) findViewById(R.id.share);
	 		search = (Button) findViewById(R.id.search);
	 		home = (Button) findViewById(R.id.home);
	 		textState = (TextView)findViewById(R.id.textState);
	 		songName = (TextView)findViewById(R.id.titleSong);
	 		songName.setSelected(true);
	 		textState.setText(getString(R.string.detenido));
	 		Bundle extras = getIntent().getExtras();
	 		if(extras!=null)
	 		{
	 			cNotice = extras.getBoolean("Retorno");
	 			Log.i("Retorno = ", ""+cNotice);
	 			home.setEnabled(cNotice);
	 			home.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_home));
	 		}
	 		
	 		String title = getString(R.string.efrradiotitle).toLowerCase();
	 		name.setSelected(true);
	 		name.setTypeface(fontSegoe);
	 		name.setTextSize(50);
	 		name.setText(title);
	 		name.setTextColor(Color.WHITE);
	 		nameApp.setTypeface(fontMain);
	 		name.setSelected(true);
	 		
	 		if(!radioIsRun){
	 			//stop.setEnabled(false);
	 			share.setEnabled(false);
	 			search.setEnabled(false);
	 		}
	 		

	    }
	    
	   
	  
	    /**------Botones onClick asignados en el xml--------*/
	    
	   //Enciende la radio y activa el Servicio RadioService 
	  public void radioOnOff(View v) throws IOException{
		  if (!radioIsRun||!EMISORA.equals(radio)) 
		  {
			  
			  if(checkConnectivity())
			  {
				  if(radioIsRun)
				  {
					  stopService(radioService);
					  Log.i(TAG, "Servicio detenido");
				  }
				 
				  radioService.putExtra("radio", "efr");
				  radioService.putExtra("streamRadio", "http://radio.everfreeradio.com:5800/stream/1/;%20stream.mp30");
				  radioService.putExtra("currentSong", "http://relay3.everfreeradio.com:8000/currentsong?sid=1");
				  
				  
				  startService(radioService);
				  play.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_load));
				  play.setEnabled(false);
			  }
			  
		  }
		  else if(radioIsRun)
		  {
			  	radioIsRun = false;	    
	           
	 			share.setEnabled(false);
	 			share.setBackgroundDrawable(null);
	 			search.setEnabled(false);
	 			search.setBackgroundDrawable(null);
	 			stopService(radioService);
	 			songName.setText("---");
	 			textState.setText(getString(R.string.detenido));
	 			
	 			
	 			play.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_play));
		  }
	  }
	  
	  
	  //Comparte la cancion actual en redes sociales
	  public void goShareSong(View v){
		  if(prevSong==null||prevSong.equals("---")||prevSong.equals(getString(R.string.recibiendo))){
			  Toast.makeText(this, R.string.errorEFR, Toast.LENGTH_LONG).show();
		  }
		  else
		  {		
			  	
				Intent shareAction = new Intent(Intent.ACTION_SEND);
				Log.v("Canción a compartir", prevSong);
				shareAction.setType("text/plain");
				shareAction.putExtra(Intent.EXTRA_TEXT, getString(R.string.listen)+" "+prevSong 
																	+ " " + getString(R.string.on)+ " @EverFreeNetwork " 
																	+ getString(R.string.via)+" #MyLittleHub");
				
				startActivity(Intent.createChooser(shareAction, getString(R.string.share)));
				
	      }
		
	  }
	  public void goSearch(View v){
		  if(prevSong==null||prevSong.equals("---")||prevSong.equals(getString(R.string.recibiendo))){
			  Toast.makeText(this, R.string.errorEFR, Toast.LENGTH_LONG).show();
		  }
		  else
		  {		
			  
			  Intent searchSong = new Intent(Intent.ACTION_WEB_SEARCH);
			  //searchSong.setPackage("com.google.android.youtube");
			  searchSong.setPackage("com.google.android.googlequicksearchbox");
			  searchSong.putExtra(SearchManager.QUERY, prevSong);
			  searchSong.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			  try
			  {
				  startActivity(searchSong);
			  }
			  catch(Exception e)
			  {
				  Toast.makeText(this, R.string.errorSearch, Toast.LENGTH_LONG).show();
			  }
			 
	      }
		
	  }
	  
	  
	 public void home(View v)
	 {
		 Intent homeI = new Intent(Everfree.this, MainActivity.class);
		 startActivity(homeI);
		 finish();
		 
	 }
	  /**-------Fin seccion de botones---------*/
	  
	  
	  
	 
	  
	  private BroadcastReceiver broadcastStateReceiver = new BroadcastReceiver() {
			
			
			public void onReceive(Context context, Intent stateIntent) {
				
				radio = stateIntent.getStringExtra("radio");
				radioIsRun = stateIntent.getBooleanExtra("running", false);
				Log.v(TAG + " radioIsRun?", radioIsRun+"");
				
				if(EMISORA.equals(radio))
				{
					play.setEnabled(true);
					//radioIsRun = stateIntent.getBooleanExtra("state", false);
					currentSong = stateIntent.getStringExtra("currentSong");
					
					
					
					estado = stateIntent.getIntExtra("estado", 0);
					
					switch (estado)
					{
						case 1:
							textState.setText(getString(R.string.conectandoRadio));
							play.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_load));
							play.setEnabled(false);
							break;
							
						case 2:
							play.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stop));
							
							textState.setText(getString(R.string.reproduciendo));
							play.setEnabled(true);
							break;
							
						case 3:
							
							textState.setText(getString(R.string.pausa));
							break;
							
						case 5:
							textState.setText(getString(R.string.errorConRadio));
							play.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_play));
							break;
							
					}
					
					
					
					
					if(!prevSong.equals(currentSong))
					{
						prevSong=currentSong;
						songName.setText(prevSong);
						share.setEnabled(true);
						share.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_share));
						search.setEnabled(true);
						search.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_search));
					}
					/*else
					{
						prevSong=currentSong;
						songName.setText(prevSong);
						share.setEnabled(true);
						share.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_share));
						search.setEnabled(true);
						search.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_search));
											
					}*/
				}
			}
		};
		
/** Comprueba si el dispositivo está actualmente conectado a Internet**/
		
		private boolean checkConnectivity() {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

			if ((null != cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) && 
					cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting())
					|| cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
							.isConnectedOrConnecting())
			{
				isOnline = true;
			}
				
			else
			{
				isOnline = false;
				Toast.makeText(this, R.string.errordered, Toast.LENGTH_LONG).show();
			}
			Log.i("¿Conectado a internet?", ""+isOnline);
			return isOnline;
		}
		
/**---------------------------------------------------------------*/
		
		protected void onResume() {
			// Register broadcast receiver
			if (!mBufferBroadcastIsRegistered) {
				registerReceiver(broadcastStateReceiver, new IntentFilter(
						RadioService.BROADCAST_STATE));
				mBufferBroadcastIsRegistered = true;
			}
			super.onResume();
		}
		
		protected void onDestroy(){
			if (mBufferBroadcastIsRegistered) {
				unregisterReceiver(broadcastStateReceiver);
				mBufferBroadcastIsRegistered = false;
			}
			super.onDestroy();
		}
		
	    
}