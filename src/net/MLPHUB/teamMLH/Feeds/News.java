package net.MLPHUB.teamMLH.Feeds;

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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class News extends Activity {
	private static String URL_BASE;
	private static String URL_RSS;

	Context context;
	private ListView listFeed;
	private ArrayList<Feed> feed;
	private NewsAdapter feedAdapter;
	private TareaDescarga task;
	private TareaDescargaImagen tdi; 
	private SharedPreferences prefs;

	private String nFeed, uFeed, wFeed, hFeed, sFeed, tFeed;
	private Button update;
	private RelativeLayout appbar;

	String imageSource = null;

	class ImageGetter implements Html.ImageGetter {
		public Drawable getDrawable(String source) {
			imageSource = source;
			return new BitmapDrawable();
		}
	};
	
/*welovefine http://feeds.feedburner.com/WelovefineShop-NewProducts*/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mlh_feeds_news);
		
		Typeface fontMain = Typeface.createFromAsset(getAssets(),
				"fonts/fontmain.TTF");
		Typeface fontSegoe = Typeface.createFromAsset(getAssets(),
				"fonts/SEGOEUIL.TTF");
		Bundle extras = getIntent().getExtras();
		TextView name = (TextView) findViewById(R.id.textTitleFeed);
		TextView appName = (TextView) findViewById(R.id.news);
		if(extras != null)
		{
			nFeed = extras.getString("NameFeed");
		    uFeed = extras.getString("FeedUrl");
		    wFeed = extras.getString("WebUrl");
		    hFeed = extras.getString("Hashtag");
		    sFeed = extras.getString("ShortName");
		    tFeed = extras.getString("Type");
		}
		
		appName.setTypeface(fontMain);
		appName.setText(getString(R.string.feedTitle).toUpperCase());
		name.setTypeface(fontSegoe);
		name.setTextSize(50);
		name.setText(sFeed);
		name.setSelected(true);
		int back=R.drawable.mane6;
		getWindow().setBackgroundDrawableResource(back);
		prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);
	
		appbar = (RelativeLayout)findViewById(R.id.appbar);
		

		context = getApplicationContext();
		
		

		URL_BASE = wFeed;
		URL_RSS = uFeed;

		feed = new ArrayList<Feed>();
		feedAdapter = new NewsAdapter(context, R.layout.mlh_feeds_newsadapter, feed);

		listFeed = (ListView) findViewById(R.id.itemList);
		listFeed.setAdapter(feedAdapter);
		listFeed.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// en "id" tenemos el número de fila seleccionada
				Feed entry = feed.get(position);
				Intent loadFeed = new Intent(News.this, NoticiaCompleta.class);
				loadFeed.putExtra("Titulo", entry.getTitle());
				loadFeed.putExtra("HtmlText", entry.getHtmlText());
				loadFeed.putExtra("URL", entry.getLink());
				if(hFeed == null)
				{
					loadFeed.putExtra("Hashtag", sFeed);
				}
				else
				{
					loadFeed.putExtra("Hashtag", hFeed);
				}
				startActivity(loadFeed);
								
			}
		});
		
		launchDownloadFeeds();
	}

	public boolean onCreateOptionsMenu(Menu m) {
		getMenuInflater().inflate(R.menu.menufeeds, m);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reload:
			launchDownloadFeeds();
			break;
		case R.id.URL:
			Intent browserAction = new Intent(Intent.ACTION_VIEW,
					Uri.parse(URL_BASE));
			startActivity(browserAction);
			break;
		}
		return true;
	}

	void launchDownloadFeeds() {
		try {
			task = new TareaDescarga();
			task.execute(new URL(URL_RSS));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/** Selección de botones*/
	
	public void update(View v)
	{
		Log.i("AppBar Event", "click refresh");
		tdi.cancel(true);
		launchDownloadFeeds();
		
	}
	public void expand(View v)
	{
		Log.i("AppBar Event", "click expand");
		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) appbar.getLayoutParams();
		Log.v("NewsTAG", "Layout params:"
						+"\nparamHeight: "+appbar.getHeight()
						+"\nparamWidth: "+appbar.getWidth());

		if(params.topMargin != -60)
		{
			params.topMargin = -60;
		}
		else
		{
			params.topMargin = -50;
		}
		//params.setMargins(appbar.getLeft(),appbar.getTop()+60, appbar.getRight(), appbar.getBottom());
		appbar.setLayoutParams(params);
		//appbar.setFadingEdgeLength(60);
		
	}

	/** ---------------------------------Clase TareaDescarga-----------------------------------------------------*/
	
	public class TareaDescarga extends AsyncTask<URL, String, List<Feed>> {

		ArrayList<Feed> downloadedFeeds;
		ProgressDialog progressDialog;
		boolean error = false;

		@Override
		protected List<Feed> doInBackground(URL... params) {
			try {
				URL url = params[0];
				downloadedFeeds = new ArrayList<Feed>();

				XmlPullParserFactory parserCreator = XmlPullParserFactory
						.newInstance();
				XmlPullParser parser = parserCreator.newPullParser();
				parser.setInput(url.openStream(), null);
				int parserEvent = parser.getEventType();
				int nItems = 0;

				while (parserEvent != XmlPullParser.END_DOCUMENT) {
					switch (parserEvent) {
					case XmlPullParser.START_TAG:
						String tag = parser.getName();
						/*
						 * for (int i = 0; i<tag.length();i++) {
						 * System.out.println("Tag: "+ tag); }
						 */
						if (tag.equalsIgnoreCase("entry")||tag.equalsIgnoreCase("item")) {

							publishProgress(getString(R.string.descargandofeed)
									+ " " + (++nItems));
							Feed feed = new Feed();
							parserEvent = parser.next();
							boolean itemClosed = false;

							while (parserEvent != XmlPullParser.END_DOCUMENT
									&& !itemClosed) {
								switch (parserEvent) {
								case XmlPullParser.START_TAG:

									tag = parser.getName();
									if (tag.equalsIgnoreCase("title")) {
										feed.setTitle(parser.nextText());
										// System.out.println(feed.getTitle());
									}

									if (tag.toLowerCase().contains("updated")||tag.toLowerCase().contains("date")) {
										feed.setDate(parser.nextText());
										// System.out.println(feed.getDate());
									}

									if (tag.equalsIgnoreCase("feedburner:origLink")||tag.equalsIgnoreCase("target")) {
										feed.setLink(parser.nextText());
										System.out.println(" Link: "
												+ feed.getLink());
									}
									
									if (tag.equalsIgnoreCase("link")) {
										feed.setLink(parser.getAttributeValue(null,
												"href"));
										if(feed.getLink()==null)
										{
											feed.setLink(parser.nextText());
										}
										System.out.println(" Link: "
												+ feed.getLink());
									}
									
									
									

									if (tag.equalsIgnoreCase("content")||tag.equalsIgnoreCase("description")||tag.equalsIgnoreCase("summary")) {
										String textoHtml = parser.nextText();
										Spanned text = Html.fromHtml(textoHtml,
												new ImageGetter(), null);
										feed.setDesc(text);
										feed.setHtmlText(textoHtml);

									}
									if (tag.equalsIgnoreCase("media:thumbnail")||tag.equalsIgnoreCase("logo")) {
										/*
										 * Obtiene la URL desde el atributo
										 * ~Alexei
										 */
										String linkImage = parser
												.getAttributeValue(null, "url");
										System.out.println(" Link img: "
												+ linkImage);
										feed.setImageLink(linkImage);
									}
									break;// end case
								case XmlPullParser.END_TAG:

									tag = parser.getName();
									if (tag.equalsIgnoreCase("entry")||tag.equalsIgnoreCase("item")) {
										itemClosed = true;
										downloadedFeeds.add(feed);
									}
									break; // end case
								}// end switch parse event
								
								// Si hago avanzar al parser cuando fijo
								// [itemClosed] en true, saldrá del ciclo y
								// avanzará una vez más. Por eso es que se salta
								// un elemento.
								//
								// ~Alexei
								if (!itemClosed) {
									parserEvent = parser.next();
									Log.i("EqCh", "Línea 222: Evento: "
											+ getEventString(parserEvent)
											+ " Tag: " + parser.getName());
								}
							}// end while
						}// end if item
						break;
					}// end switch start case
					parserEvent = parser.next();
				}// end while

				parserEvent = parser.next();

			} // end try

			catch (Exception e) {
				Log.e("Net", "Error in network call", e);
				error = true;
			}// end catch

			return downloadedFeeds;
		}// end list 
		
		private String getEventString(int parserEvent) {
			String _out = String.valueOf(parserEvent);

			if (parserEvent == XmlPullParser.END_DOCUMENT)
				_out = "END_DOCUMENT";
			if (parserEvent == XmlPullParser.END_TAG)
				_out = "END_TAG";
			if (parserEvent == XmlPullParser.START_TAG)
				_out = "START_TAG";
			if (parserEvent == XmlPullParser.TEXT)
				_out = "TEXT";

			return _out;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// feedAdapter.clear();
			progressDialog = ProgressDialog.show(News.this,
					getString(R.string.wait),
					getString(R.string.conectando)+" "+nFeed, true, true);
			progressDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					task.cancel(true);
				}
			});
		}

		protected void onCancelled() {
			super.onCancelled();
			feedAdapter.clear();
			downloadedFeeds = new ArrayList<Feed>();
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			super.onProgressUpdate(progress);
			progressDialog.setMessage(progress[0]);
			feedAdapter.notifyDataSetChanged();
		}

		protected void onPostExecute(List<Feed> result) {
			super.onPostExecute(result);
			for (Feed e : downloadedFeeds) {
				feedAdapter.add(e);
			}

			feedAdapter.notifyDataSetChanged();
			
			progressDialog.dismiss();
			if (error) {
				Toast.makeText(context, R.string.errordered, Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(context, R.string.complete, Toast.LENGTH_LONG).show();
				
			}
			launchDownloadImages();
		}

	}

	
	private void launchDownloadImages()
	{ 
		tdi = new TareaDescargaImagen();		
		tdi.execute(feed);	
	}
	
	
	
	
	
	 /**----------------------------------- AsyncTask Imagenes en segundo plano ----------------------**/
	
	
	class TareaDescargaImagen extends AsyncTask<List<Feed>, String, Drawable>{
	  
	  @Override 
	  protected Drawable doInBackground(List<Feed>... arg0) {
		  List<Feed> fdL = arg0[0]; 
		  int imagenesSinCargar = fdL.size();
		 
		  while(imagenesSinCargar > 0){ 
			  
			  if (isCancelled()){ Log.w("Tarea Cancelada", "Break de tarea"); break; }
			  
			  imagenesSinCargar = fdL.size(); for(Feed fd:fdL)
			  { 
				  if(fd.getImg()==null || fd.getImg().getIntrinsicHeight() <= 0)
				  {		  // Reintento necesario? 
					  try
					  { 
						  fd.loadImagen(fd.getImageLink());
						  System.out.println("link thumb: "+fd.getImageLink().toString());
						  publishProgress(""); 
					  } 
					  catch(Exception e)
					  {
						  //fd.setImg(getResources().getDrawable(R.drawable.nopicture)); 
					  } 
				   } 
				   else
				   {
					  imagenesSinCargar --; 
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
	  
	 	protected void onPostExecute(Drawable result) {
	 		super.onPostExecute(result); 
	 	
	 		feedAdapter.notifyDataSetChanged();
	 		
	 	}
	  
	    protected void onProgressUpdate(String... values) {
	 		super.onProgressUpdate(values); 
	 		
	 		feedAdapter.notifyDataSetChanged(); 
	 		
	 	} 
	    
	 	protected void onCancelled() {
			super.onCancelled();
			
			tdi.cancel(true);
	 	
	 	}
	 	
	}
	 
}