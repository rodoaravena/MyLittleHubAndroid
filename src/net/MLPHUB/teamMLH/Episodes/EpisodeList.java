package net.MLPHUB.teamMLH.Episodes;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import net.MLPHUB.teamMLH.R;
import net.MLPHUB.teamMLH.R.drawable;
import net.MLPHUB.teamMLH.R.id;
import net.MLPHUB.teamMLH.R.layout;
import net.MLPHUB.teamMLH.R.menu;
import net.MLPHUB.teamMLH.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EpisodeList extends Activity {
	private static String URL_EPLIST, URL_THUMBS, URL_IMAGES;

	Context context;
	ListView listView;
	ArrayList<Episode> episodes;
	EpisodeListAdapter epAdapter;
	DownloadTask task = new DownloadTask();
	int season = 0;
	int flag;
	String titleEp, directorEp, writerEp, synopsisEp, dirimgEp, numberEp, seasonEp;
	Bitmap img;
	TextView seasons, appModule;
	String imageSource = null;
	ImageView iv;

	class ImageGetter implements Html.ImageGetter {
		public Drawable getDrawable(String source) {
			imageSource = source;
			return new BitmapDrawable();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mlh_episodes_episodelist);
		Typeface fontMain = Typeface.createFromAsset(getAssets(),
				"fonts/fontmain.TTF");
		Typeface fontSegoe = Typeface.createFromAsset(getAssets(),
				"fonts/SEGOEUIL.TTF");
		seasons = (TextView) findViewById(R.id.season);
		appModule = (TextView) findViewById(R.id.textTitleEpisode);
		
		appModule.setText(getString(R.string.epilisttitle).toUpperCase());
		seasons.setTypeface(fontSegoe);
		seasons.setTextSize(50);
		seasons.setText(R.string.all);
		seasons.setSelected(true);
		appModule.setTypeface(fontSegoe);

		context = getApplicationContext();

		URL_EPLIST = getString(R.string.urlEpList);
		URL_THUMBS = getString(R.string.urlThumbs);
		URL_IMAGES = getString(R.string.urlImages);

		episodes = new ArrayList<Episode>();
		epAdapter = new EpisodeListAdapter(context, R.layout.mlh_episodes_episodelistadapter, episodes);

		listView = (ListView) findViewById(R.id.itemList);
		
		listView.setAdapter(epAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1,
				int position, long id) {
			// en "id" tenemos el número de fila seleccionada
			Episode entry = episodes.get(position);
			
			Intent loadEpisode = new Intent(EpisodeList.this, EpisodeDescription.class);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			System.out.println(entry.get_synopsis());
			
			titleEp = entry.get_title().toString();
			directorEp = entry.get_director().toString();
			writerEp = entry.get_writer().toString();
			dirimgEp = entry.get_image().toString();
			synopsisEp = entry.get_synopsis().toString();
			numberEp = ""+entry.get_number();
			seasonEp = ""+entry.get_season();
			
			/*img = entry.getImage();
			img.compress(Bitmap.CompressFormat.PNG, 100, baos);*/
			//byte[] res = baos.toByteArray();
			loadEpisode.putExtra("title", titleEp);
			loadEpisode.putExtra("director", directorEp);
			loadEpisode.putExtra("writer", writerEp);
			loadEpisode.putExtra("img", dirimgEp);
			loadEpisode.putExtra("synopsis", synopsisEp);
			loadEpisode.putExtra("number", numberEp);
			loadEpisode.putExtra("season", seasonEp);
			
			
			startActivity(loadEpisode);
		}
	});

		launchDownloadEpisodesAll();
	}
/*
 * Se a comentado este codigo debido a que causa force-down al momento
 * de que se selecciona una temporada y está descargando las imagenes en segundo plano
 * 
	public boolean onCreateOptionsMenu(Menu m) {
		getMenuInflater().inflate(R.menu.epilistall, m);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
			switch (item.getItemId()) {
				case R.id.reload00:
					if(season!=0){
						season = 0;						
						task.getEpisodes();
						seasons.setText(R.string.all);
					}
					break;
				case R.id.reload01:
					if(season!=1){
						season=1;
						task.getEpisodes();
						seasons.setText(R.string.season1);
					}
				break;
				case R.id.reload02:
					if(season!=2){
						season=2;
						task.getEpisodes();
						seasons.setText(R.string.season2);
					}
					break;
				
				case R.id.reload03:
					if(season!=3){
						season=3;
						task.getEpisodes();
						seasons.setText(R.string.season3);
					}
					break;
			}
		
		
		return true;
	}
	*/


	void launchDownloadEpisodesAll() {
		try {
			task = new DownloadTask();
			task.execute(new URL(URL_EPLIST));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public class DownloadTask extends AsyncTask<URL, String, List<Episode>> {

		ArrayList<Episode> downloadedEpi;
		ProgressDialog progressDialog;
		boolean error = false;

		@Override
		protected List<Episode> doInBackground(URL... params) {
			try {
				URL url = params[0];
				downloadedEpi = new ArrayList<Episode>();
				XmlPullParserFactory parserCreator = XmlPullParserFactory
						.newInstance();
				XmlPullParser parser = parserCreator.newPullParser();
				parser.setInput(url.openStream(), null);
				int parserEvent = parser.getEventType();
				//int nItems = 0;
				

				while (parserEvent != XmlPullParser.END_DOCUMENT) {
					switch (parserEvent) {
					case XmlPullParser.START_TAG:
						String tag = parser.getName();
						/*
						 * for (int i = 0; i<tag.length();i++) {
						 * System.out.println("Tag: "+ tag); }
						 */
						// Toast.makeText(context, R.string.descargandofeed,
						// Toast.LENGTH_LONG).show();
						
						if (tag.equalsIgnoreCase("Episode")) {
							
							System.out.println("PAse TAG EPISODE");
							publishProgress(getString(R.string.descargandoepil)
									/*+ " " + (++nItems)*/);
							Episode _ep = new Episode();
							//flag = Integer.parseInt(parser.getAttributeValue(null, "Season"));
							
								System.out.println("Season: "+ season);
							_ep.set_season(Integer.parseInt(parser
									.getAttributeValue(null, "Season")));

							_ep.set_title(parser.getAttributeValue(null,
									"Title"));

							android.util.Log.v("EPLIST", _ep.get_title());

							
							_ep.set_director(" "+parser.getAttributeValue(null,
									"Director"));

							_ep.set_number(Integer.parseInt(parser
									.getAttributeValue(null, "Number")));

							_ep.set_picture(URL_THUMBS+parser.getAttributeValue(null,
									"Picture"));
							
							_ep.set_image(URL_IMAGES+parser.getAttributeValue(null,
									"Picture"));
							

							_ep.set_writer(" "+parser.getAttributeValue(null,
									"Writer"));

							String _desc = parser.nextText();
							Spanned text = Html.fromHtml(_desc,
									new ImageGetter(), null);

							_ep.set_synopsis(text);
							
							// parserEvent = parser.next();
							boolean itemClosed = false;

							parserEvent = parser.getEventType();

							while (parserEvent != XmlPullParser.END_DOCUMENT
									&& !itemClosed) {
								switch (parserEvent) {
								case XmlPullParser.END_TAG:

									tag = parser.getName();
									if (tag.equalsIgnoreCase("Episode")) {
										itemClosed = true;
										downloadedEpi.add(_ep);
										
									}
									break; // end case
								}// end switch parse event
								parserEvent = parser.next();
							}// end while
						// }
						}// end if item
						break;
					}// end switch start case
					parserEvent = parser.next();
				}// end while all
				
				parserEvent = parser.next();

			} // end try

			catch (Exception e) {
				Log.e("Net", "Error in network call", e);
				error = true;
			}// end catch
			
			return downloadedEpi;
		}// end list episodes

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// feedAdapter.clear();
			Toast.makeText(context, R.string.conectandoSer, Toast.LENGTH_LONG);
			progressDialog = ProgressDialog.show(EpisodeList.this,
					getString(R.string.wait),
					getString(R.string.conectandoSer), true, true);
			progressDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					downloadedEpi = new ArrayList<Episode>();
					task.cancel(true);
					
				}
			});
		}

		protected void onCancelled() {
			super.onCancelled();
			// feedAdapter.clear();
			downloadedEpi = new ArrayList<Episode>();
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			super.onProgressUpdate(progress);
			//epAdapter.clear();
			
			progressDialog.setMessage(progress[0]);
			epAdapter.notifyDataSetChanged();

		}

		protected void onPostExecute(List<Episode> result) {
			super.onPostExecute(result);
			epAdapter.clear();
			getEpisodes();
			epAdapter.notifyDataSetChanged();
			
			progressDialog.dismiss();
			if (error) {
				Toast.makeText(context, R.string.errordered, Toast.LENGTH_LONG)
						.show();
			}
			else{
				Toast.makeText(context, R.string.complete, Toast.LENGTH_LONG)
				.show();
			}
			 launchDownloadImages();
		}
		
		public void getEpisodes(){
			epAdapter.clear();
			for (Episode e : downloadedEpi) {
				flag = e.get_season();
				System.out.println("flag = "+flag);
				
				if(season==flag||season==0){
					System.out.println("i= "+flag);
					epAdapter.add(e);
				}
			}
			
		}

	}
	
	void launchDownloadImages(){ 
		TareaDescargaImagen tdi = new TareaDescargaImagen(); 
		tdi.execute(episodes); 
		}
	 /**Clase Tarea descarga img private*/
	class TareaDescargaImagen extends AsyncTask<List<Episode>, String, Drawable>{
	  
	  @Override 
	  protected Drawable doInBackground(List<Episode>... arg0) {
		  
		  List<Episode> fdL = arg0[0]; 
		  int thumbsSinCargar = fdL.size();
		  int imagenesSinCargar = fdL.size();
		  while(thumbsSinCargar > 0){ 
			  thumbsSinCargar = fdL.size(); for(Episode fd:fdL)
			  { 
				  if(fd.getThumbImg()==null || fd.getThumbImg().getIntrinsicHeight() <= 0)
				  {		  // Reintento necesario? 
					  try
					  { 
						  fd.loadThumbImage(fd.get_picture());
						  System.out.println("link thumb: "+fd.get_picture().toString());
						  //fd.loadFullImage(fd.get_image());
						  thumbsSinCargar --; 
						  System.out.println("ISC = "+thumbsSinCargar);
						  publishProgress(""); 
					  } 
					  catch(Exception e)
					  {
						  fd.setThumbImg(getResources().getDrawable(R.drawable.nopicture)); 
					  } 
				   } 
				   else
				   {
					   thumbsSinCargar --; 
					  System.out.println("ISC = "+thumbsSinCargar);
				   } 
				 } 
			  try {
				  Thread.sleep(1000); 
				  } 
			  catch	(InterruptedException e) 
			  { 
				  
			  } 
		  } 
		  
		  return null; 
	}
	  
	 	@Override protected void onPostExecute(Drawable result) {
	 		super.onPostExecute(result); 
	 		epAdapter.notifyDataSetChanged();
	 		}
	  
	 	@Override protected void onProgressUpdate(String... values) {
	 		super.onProgressUpdate(values); epAdapter.notifyDataSetChanged(); 
	 		} 
	}
	
	public void onBackPressed(){
		finish();
	}
	
}


