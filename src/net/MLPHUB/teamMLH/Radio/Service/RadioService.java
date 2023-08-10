package net.MLPHUB.teamMLH.Radio.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.MLPHUB.teamMLH.R;
import net.MLPHUB.teamMLH.Radio.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;// Necesita JAR android-v4.jar pero está en fase de prueba aún no está funcional


public class RadioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
{
	
  public static final String BROADCAST_STATE = "net.MLPHUB.teamMLH.Radio"; // este es un tag para cuando se use el servicio, con este se identifica en la actividad principal. 
  private boolean isPlaying = false;
  private int NOTIFICATION = R.string.app_name, estado = 0;
  private NotificationManager mNotificationManager;
  private String TAG = getClass().getSimpleName();
  private MediaPlayer mp = null;
  private PhoneStateListener phoneState;
  private TelephonyManager telManager;
  private boolean pauseByCall = false, req = true;
  private WifiLock wifiLock = null;
  private IBinder mBinder = new LocalBinder();
  private String song = "";
  private Handler handler = new Handler();
  private Timer timer = new Timer();
  private Intent sendState;
  private BufferedReader str;
  private int ms = 5;
  private ArrayList<String> paramsRadio = new ArrayList<String>();
  
  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
  
  /** Este timer permite enviar las canciones cada 10 segundos a la actividad principal y a la notificación **/
  
  
  TimerTask stateServiceLive = new TimerTask(){
      
      public void run() 
      {
          handler.post(new Runnable()
          {
              public void run() 
              {
            	  Log.v(TAG + " State", "ms = "+ms);
            	  Log.v(TAG + " Emisora actual", paramsRadio.get(0));
            	  
            	  sendState.putExtra("currentSong", song);
            	  sendState.putExtra("estado", estado);
            	  sendState.putExtra("radio", paramsRadio.get(0));
            	  sendState.putExtra("running", true);
            	  //showNotification2(); Ambos invocan para actualizar la cancion actual en la notificación
            	  showNotification();            	  
            	  if(ms==10&&req)
            	  {            		  
            		  new GetSong().execute();
            		  //sendState.putExtra("currentSong", song);     //       		  
            		  ms=0;            		  
            	  }
            	  if(estado == 5)
            	  {
            		  stopSelf();            		           		  
            	  }
            	
            	  ms++;
            	  sendBroadcast(sendState);
            	         	  
              };
          });

          
      }
  };
  
  /** showNotification2 genera una notificación touchable la cual redirije a la actividad principal, esta forma de invocar las nofificaciones está en fase de prueba **/
  
  /*private void showNotification2()
  {
	  Intent  returnRadio = new Intent(this, Everfree.class);
	  PendingIntent in = PendingIntent.getActivity(getApplicationContext(), 0, returnRadio, 0);
	  mBuilder.setContentTitle("MLPHUBTEST")
      .setContentText("PLAYING SOMETHING")
      .setSmallIcon(R.drawable.ic_mlh)
      .setOngoing(true)
      .setTicker("MLPHUBTEST");//;
	 
	  
	  mBuilder.setContentIntent(in);

	  mNotificationManager.notify(0, mBuilder.build());
  }*/
  /** showNotification genera una notificación touchable la cual redirije a la actividad principal, esta forma de invocar las nofificaciones está en fase de prueba
   *  este está funcional, descomentar para utilizar **/
  private void showNotification() {
	  //Éste metodo crea la notificacion en el área de notificación
	  
	  	String emisora = "";
	  	Intent returnRadio = null;
		Notification notification = null;
		
		if(paramsRadio.get(0).equals("efr"))
		{
			returnRadio = new Intent(this, Everfree.class);
			notification = new Notification(R.drawable.efr_logo, "Everfree Radio",0);
			emisora = "Everfree Radio";
		}
		if(paramsRadio.get(0).equals("srb"))
		{
			returnRadio = new Intent(this, SonicRadioBoom.class);
			notification = new Notification(R.drawable.srb_logo, "Sonic Radio Boom",0);
			emisora = "Sonic Radio Boom";
		}
		if(paramsRadio.get(0).equals("")||paramsRadio.get(0)==null)
		{
			estado = 5;
			return;
		}
		
	    notification.flags=Notification.FLAG_ONGOING_EVENT;
		  
		  returnRadio.putExtra("Retorno", true);
		  
	      PendingIntent pI = PendingIntent.getActivity(this, 0,
	              returnRadio, 0);// El PendingIntent establece una actividad en espera
	      
	      
	      
	         //En este punto se asignan los parametros para crear la notificacion
	       
	      
		      
		      notification.setLatestEventInfo(getApplicationContext(), "My Little Hub - "+emisora,song,pI);
		      startForeground(NOTIFICATION, notification);
	      
	      
  }
  
  
  public void onCreate() {
	  
	  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // inicia la instacia para crear una notificación
	  	//mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
   		Log.v(TAG, "onCreate Service");
   		sendState = new Intent(BROADCAST_STATE);
   		
   		
   	
   		timer.schedule(stateServiceLive, 100,1000);	   	
        
   }
  
  //Primer metodo que se llama al momento de iniciar el Servicio
  public int onStartCommand(Intent intent, int flags, int startId) {
	  
	  	Log.v(TAG,"onStartCommand");
	  	
	  	Bundle extras = intent.getExtras();
 		if(extras!=null)
 		{
 			
 			
 			
 			paramsRadio.add(extras.getString("radio"));
				
 			paramsRadio.add(extras.getString("streamRadio"));
				
 			paramsRadio.add(extras.getString("currentSong"));
	 			
 		}
	   
	    
	    telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    
	    wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)) // Necesario para mantener la conexión Wi-Fi activa y que no entre en modo espera.
			    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
	   
	    /** PhoneStateListener permite saber cuando el telefono está en uso, esto permite pausar y reanudar el streamming **/
	    phoneState = new PhoneStateListener()
	    {
	    	public void onCallStateChanged(int state, String incomingNumber){
	    		Log.v(TAG, "Starting listener");
	    		switch (state){
	    		case TelephonyManager.CALL_STATE_OFFHOOK:
	    		case TelephonyManager.CALL_STATE_RINGING:
	    			if(mp!=null){
	    				System.out.println("CALL"+pauseByCall);
	    				pause();
	    				pauseByCall=true;
	    			}
	    			break;
	    		case TelephonyManager.CALL_STATE_IDLE:
	    			if(mp!=null){
	    				if(pauseByCall){
	    					System.out.println("CALL_STATE_IDLE:"+pauseByCall);
	    					pauseByCall = false;
	    					estado = 1;
	    					mp.start();
	    				}
	    			}
	    			break;
	    		}
	    	}
	    };
	    
	    telManager.listen(phoneState, PhoneStateListener.LISTEN_CALL_STATE);//Activa el Listener del telefono
	    
	    play();
	    wifiLock.acquire(); // Activa el lock del wifi
	    //showNotification();
	    Log.v ("WIFI lock is: ", ""+wifiLock.isHeld());// Muestra por consola si el bloqueo de Wi-Fi está activo o no repondiendo con un true o false respectivamente.
	    return(START_STICKY);
  }
  
  public class LocalBinder extends Binder {
  	RadioService getService() {
          return RadioService.this;
      }
  }
  
  
  public void onDestroy() {
	  
	  //Se invoca cuando desde la actividad principal hace un stopService();
	  
	  Log.v(TAG,"onDestroy");
	 
	  if(phoneState != null){
		  telManager.listen(phoneState, PhoneStateListener.LISTEN_NONE);
	  }
	  if(isPlaying){
		  timer.cancel();
		  stopStream();
		  
	  }
	  else if (mp!=null){
		  mp=null;
		  
	  }
	  
	  if(wifiLock.isHeld())
	  {
		  wifiLock.release();//Libera el bloque del Wi-Fi
	  }
    stopForeground(true);
    mNotificationManager.cancel(0);
  }
  
  
  public IBinder onBind(Intent intent) {
	  System.out.println("onBind");
    return mBinder;
  }
  
  private void play() {
    
    Log.w(getClass().getName(), "Got to play()!");
    estado = 1;
    
    if(paramsRadio.get(1)==null||paramsRadio.get(1).equals(""))
    {
    	stopSelf();
    	estado = 5;
    }
    else
    {
    	Uri uriRadio = Uri.parse(paramsRadio.get(1)); // aquí colocas la dirección del stream
        
        

        //Uri uriRadio = Uri.parse("http://radio.everfreeradio.com:5800/stream/1/;%20stream.mp3");//EverFree Radio http://78.47.53.77:5800/stream/2/
    	
    	

    	try {
    		 
    		if (mp == null) {
    			mp = new MediaPlayer();
    			
    		} 
    		else {
    			mp.stop();
    			mp.reset();
    		}
    		mp.setDataSource(this,uriRadio); 
    		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    		mp.setOnPreparedListener(this);
    		mp.setOnErrorListener(this);
    		mp.prepareAsync();

    		Log.d(TAG, "LoadClip Done");
    		 			
    	} 
    	catch (Throwable t) {
    		Log.d(TAG, t.toString());
    		
    	}
    }
    
  }
  
  private void pause(){
	  Log.w(getClass().getName(), "Got to pause()!");
	  estado = 3;
	  mp.pause();
  }
  
 private void stopStream() {
      Log.w(getClass().getName(), "Got to stop()!");
      mp.stop();
      mp=null;
      isPlaying=false;
      estado = 4;
      Log.v("Stop MP", "mp = "+mp+" isPlaying = "+isPlaying);
  }
  
  public boolean onError(MediaPlayer mp, int what, int extra) {
	  //Ante cualquier error se activa este metodo para que la actividad no sufra un force down
		StringBuilder sb = new StringBuilder();
		sb.append("Media Player Error: ");
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			sb.append("Not Valid for Progressive Playback");
			
			sendState.putExtra("Error", getString(R.string.errorConRadio));
			sendBroadcast(sendState);
			//this.onDestroy();
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			sb.append("Server Died");
			
			sendState.putExtra("Error", "error");
			sendBroadcast(sendState);
			
			
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			sb.append("Unknown");
			
			sendState.putExtra("Error", "error");
			sendBroadcast(sendState);
			
			
			break;
		default:
			sb.append(" Non standard (");
			//Toast.makeText(this,getString(R.string.errordered), Toast.LENGTH_LONG).show();
			sendState.putExtra("Error", "error");
			sendBroadcast(sendState);
			//this.onDestroy();
			sb.append(what);
			sb.append(")");
		}
		
		sb.append(" (" + what + ") ");
		sb.append(extra);
		Log.e(TAG, sb.toString());
		//this.onDestroy();
		estado = 5;
		return true;
	}


  public void onPrepared(MediaPlayer mp) {
	Log.d(TAG, "Stream is prepared");
	//Al momento que el Stream esta listo para reproducirse automaticamente inicia el Stream.
	mp.start();
	estado = 2;
	isPlaying = true;
  }
  
  private class GetSong extends AsyncTask<Void, Void, Void>
  {	   
	   
	  
	   protected Void doInBackground(Void... params) {
		   Log.v("Service AsyncTask", "Inicio de descarga de la canción");
		   req=false;
	       URL textUrl;
	       
	       try {
	    	   //textUrl = new URL("http://relay3.everfreeradio.com:8000/currentsong?sid=1");
	    	   textUrl = new URL(paramsRadio.get(2));

	    	   BufferedReader bufferReader 
	    	   		= new BufferedReader(new InputStreamReader(textUrl.openStream()));
	    	   
	    	   String StringBuffer;
	    	   String stringText = "";
	    	   while ((StringBuffer = bufferReader.readLine()) != null) {
	    		   stringText += StringBuffer;   
	    	   }
	    	   bufferReader.close();

	    	   song = stringText;
	    	   Log.i("Service AsyncTask", "Descarga realizada con exito");
	    	   req=true;
	       } 
	       catch (MalformedURLException e) {
	    	   
	    	   e.printStackTrace();
	    	   
	    	   //textResult = e.toString(); 
	    	   Log.e("Service AsyncTask", "Se produjo un error en la descarga");
	    	   req=true;
	       } 
	       catch (IOException e) {
	    	  
	    	   e.printStackTrace();
	    	   //textResult = e.toString(); 
	    	   req=true;
	    	   Log.e("Service AsyncTask", "No se pudo codificar cadena");
	    	   
	       }
	       
	       
	       
	       
		   return null;
		   
	   }
	   
	   @Override
	   protected void onPostExecute(Void result) {
		   super.onPostExecute(result);   
		   req=true;
		   Log.v("Service AsyncTask", "Finalizó el uso de la clase");
		   
	   }

  }
  
	

}