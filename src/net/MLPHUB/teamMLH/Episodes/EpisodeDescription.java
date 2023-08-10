package net.MLPHUB.teamMLH.Episodes;

import net.MLPHUB.teamMLH.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EpisodeDescription extends Activity {
	

	private String title, director, writer, synopsis, dirimg, number, season;
	private TextView nameEpisode, episodeDirector, episodeWriter, episodeSynopsis;
	private ImageView iv;
	private Drawable downloadedImg;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mlh_episodes_episodedescription);
		Bundle extras = getIntent().getExtras();
		iv = (ImageView) findViewById(R.id.episodeImage);
		nameEpisode = (TextView) findViewById(R.id.episodeTitle);
        episodeDirector = (TextView) findViewById(R.id.episodeDirector);
        episodeWriter = (TextView) findViewById(R.id.episodeWriter);
        episodeSynopsis = (TextView) findViewById(R.id.episodeContent);
        if(extras !=null)
        {
        	//Se reciben los datos desde EpisodeList
        	title = extras.getString("title");
        	director = extras.getString("director");
        	writer = extras.getString("writer");
        	synopsis = extras.getString("synopsis");
        	dirimg = extras.getString("img");
        	number = extras.getString("number");
        	season = extras.getString("season");
        }
        
        launchDownloadImages();
        
		nameEpisode.setText(title);
		nameEpisode.setSelected(true);
		episodeDirector.setText(director);
		episodeWriter.setText(writer);
		episodeSynopsis.setText(synopsis+"\n");
	}
	
	
	/**----------------- Busqueda de episodio----------------**/
	
	public void goSearch(View v){
		 
			  
			  Intent searchEpisode = new Intent(Intent.ACTION_SEARCH);
			  searchEpisode.setPackage("com.google.android.youtube");
			  searchEpisode.putExtra("query", " MLP S"+season+" E"+number);
			  searchEpisode.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			  try
			  {
				  startActivity(searchEpisode);
			  }
			  catch(Exception e)
			  {
				  Toast.makeText(this, R.string.errorSearch, Toast.LENGTH_LONG).show();
			  }
		
	 }
	

	
	void launchDownloadImages(){ 
		TareaDescargaImagen tdi = new TareaDescargaImagen(); 
		tdi.execute(); 
		}
	
	  private class TareaDescargaImagen extends AsyncTask<Void, Void, Void>{
		   
		 protected Void doInBackground(Void... params) {
			   
		       try {
		    	   InputStream is = (InputStream) new URL(dirimg).getContent();
		   		downloadedImg = Drawable.createFromStream(is, "src");
		   		//iv.setBackground(downloadedImg);   
		   		
		       } catch (MalformedURLException e) {
		    	   // TODO Auto-generated catch block
		    	   e.printStackTrace();		    	    
		       } catch (IOException e) {
		    	   // TODO Auto-generated catch block
		    	   e.printStackTrace(); 
		       }

			   return null;
			   
		   }
		   
		   @Override
		   protected void onPostExecute(Void result) {
			   
			   super.onPostExecute(result);
			   setImage();
			   
		   }

	  }
	  
	  @SuppressWarnings("deprecation")
	void setImage()
	  {
		  iv.setBackgroundDrawable(downloadedImg);
	  }

	
	public void onBackPressed(){
		finish();
	}
	
	
	
	
	
}