package net.MLPHUB.teamMLH.Options;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import net.MLPHUB.teamMLH.Feeds.MenuNews;
import net.MLPHUB.teamMLH.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetRss extends Activity {
  
  private ListView rssListView ;
  private FuentesDisponibles[] fuentesRss ;
  private ArrayAdapter<FuentesDisponibles> listAdapter ;
  private SharedPreferences fuentes;
  private Editor editState;
  private Button save;
  private int conf;
  private boolean sw = true;
  private static final String TAG = "SetRss";
  
  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) 
	  {
		    super.onCreate(savedInstanceState);
		    requestWindowFeature(Window.FEATURE_NO_TITLE);
		    setContentView(R.layout.mlh_options_setrss);
		    
		    /** Recibe extras */
		    
		    Bundle extras = getIntent().getExtras();
		    if(extras !=null)
		    {
		    	sw = extras.getBoolean("Configurado");
		    }
		    // Find the ListView resource. 
		    rssListView = (ListView) findViewById( R.id.rssListView );
		    
		    //Preferencias		    
		    fuentes = getSharedPreferences("Fuentes",Context.MODE_PRIVATE);
		    System.out.println(fuentes.getString("Name"+0, "none"));
		    editState = fuentes.edit();
		    
		    save = (Button) findViewById(R.id.btnSave);
		    
		    int fntsDsp=fuentes.getInt("fuentesTotales", 0);
		    fuentesRss = new FuentesDisponibles[fntsDsp];
		    fuentesRss = (FuentesDisponibles[]) getLastNonConfigurationInstance() ;
		    
		
		    
		    if ( fuentesRss == null ) {
		    	fuentesRss = new FuentesDisponibles[fntsDsp]; 
		    	for(int i = 0; i<fntsDsp;i++)
		        {
		      
		    	  System.out.println(fuentes.getString("Name"+i, "none")+" i= "+ i);
		          fuentesRss[i]=new FuentesDisponibles(fuentes.getString("Name"+i, "none")) ;
		        }
		    }
		    ArrayList<FuentesDisponibles> planetList = new ArrayList<FuentesDisponibles>();
		    planetList.addAll( Arrays.asList(fuentesRss) );
		    
		    // Set our custom array adapter as the ListView's adapter.
		    listAdapter = new SetRssAdapter(this, planetList);
		   // rssListView.addFooterView(saveButtonView);
		    rssListView.setAdapter( listAdapter );  
	  }
	  
	  
	  public void saveRss(View v)
	  {
		  
		  editState.commit();
		  	  
		 if(!sw)
		 {
			 save.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
			 Intent load = new Intent(SetRss.this, MenuNews.class);
			 startActivity(load);
		 }
		 save.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
		  finish();
	  }
	  
	  /** Gets Sets para la lista de fuentes*/
	  private static class FuentesDisponibles 
	  {
		    private String name = "" ;
		    private boolean checked = false ;
		    private int id = 0;
		    
		    public FuentesDisponibles() {}
		    
		    public FuentesDisponibles( String name) 
		    {
		    	this.name = name ;
		    	this.id = id;
		    }
		    public FuentesDisponibles( String name, int id,boolean checked ) 
		    {
		    	this.name = name ;
		    	this.id = id;
		    	this.checked = checked ;
		    }
		    public String getName() {
		    	return name;
		    }
		    public void setName(String name) {
		    	this.name = name;
		    }
		    public int getId(){
		    	return id;
		    }
		    public void setId(int id){
		    	this.id = id;
		    }
		    public boolean isChecked() {
		    	return checked;
		    }
		    public void setChecked(boolean checked) {
		    	this.checked = checked;
		    }
		    public String toString() {
		    	return name ; 
		    }
		    public void toggleChecked() {
		    	checked = !checked ;
		    }
	  }
	  
	  /** Holder*/
	  private static class RssViewHolder {
	    private CheckBox checkBox ;
	    private TextView textView ;
	    
	    public RssViewHolder() {}
	    public RssViewHolder( TextView textView, CheckBox checkBox ) {
	      this.checkBox = checkBox ;
	      this.textView = textView ;
	    }
	    public CheckBox getCheckBox() {
	      return checkBox;
	    }
	    public void setCheckBox(CheckBox checkBox) {
	      this.checkBox = checkBox;
	    }
	    public TextView getTextView() {
	      return textView;
	    }
	    public void setTextView(TextView textView) {
	      this.textView = textView;
	    }    
	  }
	  
	  /** Custom adapter for displaying an array of Planet objects. */
	  private class SetRssAdapter extends ArrayAdapter<FuentesDisponibles> {
	    
	    private LayoutInflater inflater;
	    
	    public SetRssAdapter( Context context, List<FuentesDisponibles> planetList ) {
	      super( context, R.layout.mlh_options_setrssadapter, R.id.rowNameFeed, planetList );
	      // Cache the LayoutInflate to avoid asking for a new one each time.
	      inflater = LayoutInflater.from(context) ;
	    }
	
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	      FuentesDisponibles fuenteOp = (FuentesDisponibles) this.getItem( position ); 

	      CheckBox checkBox ; 
	      TextView textView ; 


	      if ( convertView == null ) 
	      {
	        convertView = inflater.inflate(R.layout.mlh_options_setrssadapter, null);

	        textView = (TextView) convertView.findViewById( R.id.rowNameFeed );
	        checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox );

	        convertView.setTag( new RssViewHolder(textView,checkBox) );

	        checkBox.setOnClickListener( new View.OnClickListener() 
	        {
		          public void onClick(View v) 
		          {
			            CheckBox cb = (CheckBox) v ;
			            FuentesDisponibles feed = (FuentesDisponibles) cb.getTag();
			            feed.setChecked( cb.isChecked() );
			            System.out.println("Tag: "+cb.getTag()+" Check!: "+cb.isChecked());
			            
			            editState.putBoolean("Show"+cb.getTag(), cb.isChecked());
			            if(cb.isChecked())
			            {
			            	conf++;
			            }
			            else if(!cb.isChecked())
			            {
			            	conf--;
			            }
			            if(conf>0)
			            {
			            	save.setEnabled(true);
			            }
			            else
			            {
			            	save.setEnabled(false);
			            }
			           editState.putInt("fuentesConfiguradas", conf);
			            
			            //editState.commit();
		          }
	        });        
	      }


	      else 
	      {
	      
	        RssViewHolder viewHolder = (RssViewHolder) convertView.getTag();
	        checkBox = viewHolder.getCheckBox() ;
	        textView = viewHolder.getTextView() ;
	      }
	
	      checkBox.setTag( fuenteOp ); 
	      boolean ss = fuentes.getBoolean("Show"+fuenteOp.getName(), false);
	      checkBox.setChecked( ss);
	      textView.setText( fuenteOp.getName() );      
	      conf = fuentes.getInt("fuentesConfiguradas", 0);
	      if(conf==0)
	      {
	    	  save.setEnabled(false);
	      }
	      int fT = fuentes.getInt("fuentesTotales", 0);
	      editState.putInt("fuentesActuales", fT);
	      return convertView;
	    }
	    
	  }
	  
	  public Object onRetainNonConfigurationInstance() {
	    return fuentesRss ;
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
							
							if (tag.equalsIgnoreCase("RSSFeed")) 
							{
								
								Log.v(TAG, "Deserializando RSSFeed, por favor espere...");
								
								String Name =  parser.getAttributeValue(null,
										"Name");
								
								String FeedUrl = parser.getAttributeValue(null,
										"FeedUrl");
								
								String Type = parser.getAttributeValue(null,
										"Type");
														
								String WebUrl = parser.getAttributeValue(null,
										"WebUrl");
								
								String ShortName = parser.getAttributeValue(null,
										"ShortName");
								
								String Hashtag = parser.getAttributeValue(null,
										"Hashtag");
								
								
								
								Log.i(TAG, "Fuente encontrada: "
										  +"\nNombre: "+Name
										  +"\nFeedURL: "+FeedUrl
										  +"\nTipo: "+Type
										  +"\nWebURL: "+WebUrl
										  +"\nNombre corto: "+ShortName
										  +"\nHashtag: "+Hashtag
										  //+"\n¿Está activo?: "+rss.getBoolean("Show"+name, false)
									  );
								
								boolean itemClosed = false;
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