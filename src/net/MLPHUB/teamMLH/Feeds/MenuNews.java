package net.MLPHUB.teamMLH.Feeds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.MLPHUB.teamMLH.About;
import net.MLPHUB.teamMLH.MainActivity;
import net.MLPHUB.teamMLH.R;
import net.MLPHUB.teamMLH.Options.Preferences;
import net.MLPHUB.teamMLH.Options.SetRss;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;


public class MenuNews extends Activity{
	
	  private ListView menuListView ;
	  private ListaFeed[] opciones ;
	  private ArrayAdapter<ListaFeed> listAdapter ;
	  private SharedPreferences prefs, fuentes;
	  private String feed, nFeed, urlFeed, urlBaseFeed, hTagFeed ;
	  private int fntsDsp, fntsCnf, index;
	  
	
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.mlh_feeds_menunews);
	    Typeface fontMain = Typeface.createFromAsset(getAssets(), "fonts/fontmain.TTF");
	    TextView nameApp = (TextView)findViewById(R.id.news_module); 
	    
	    nameApp.setTypeface(fontMain);
	    nameApp.setText(getString(R.string.feedTitle).toUpperCase());
	    // Find the ListView resource. 
	    menuListView = (ListView) findViewById( R.id.rssLista );
	    //Preferencias
	    
	    
	    prefs=getSharedPreferences("Settings",Context.MODE_PRIVATE);
		int back = prefs.getInt("background", R.drawable.mane6);
	    fuentes = getSharedPreferences("Fuentes", Context.MODE_PRIVATE);
	    
		
		
		getWindow().setBackgroundDrawableResource(back);
	    // When item is tapped, toggle checked properties of CheckBox and Planet.
	    menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	     
	      public void onItemClick( AdapterView<?> parent, View item, 
	                               int position, long id) {
	        ListaFeed opcion = listAdapter.getItem( position );
	        
	        Intent optionI = new Intent(MenuNews.this, News.class);

	        nFeed = opcion.getName();
	        optionI.putExtra("NameFeed", nFeed);
	        optionI.putExtra("FeedUrl", fuentes.getString("FeedUrl"+nFeed, null));
	        optionI.putExtra("WebUrl", fuentes.getString("WebUrl"+nFeed, null));
	        optionI.putExtra("Hashtag", fuentes.getString("Hashtag"+nFeed, null));
	        optionI.putExtra("ShortName", fuentes.getString("ShortName"+nFeed, null));
	        optionI.putExtra("Type", fuentes.getString("Type"+nFeed, null));
	        
	        
	        startActivity(optionI);
	      }
	    });

	    
	    
	    
	    
	    // Create and populate planets.
	    //opciones = (ListaFeed[]) getLastNonConfigurationInstance() ;
	    
	    
	    /*if ( opciones == null ) {
	    	opciones = new ListaFeed[fntsCnf];
	    	System.out.println("sdfadsfads");
	    	for(int i = 0; i<fntsDsp;i++)
	    	{
	    		System.out.println("BLEH");
	    		System.out.println(fuentes.getString("Name"+i, "none")+" i= "+ i);
	    		feed = fuentes.getString("Name"+i, "none");
	    		if(fuentes.getBoolean("Show"+feed, false))
	    		{
	    			opciones[index]=new ListaFeed(feed);
	    			index++;
	    		}
	  	      	
	    	}
	    	 
	    }
	    ArrayList<ListaFeed> menuList = new ArrayList<ListaFeed>();
	    menuList.addAll( Arrays.asList(opciones) );
	    
	    // Set our custom array adapter as the ListView's adapter.
	    listAdapter = new MenuArrayAdapter(this, menuList);
	   
	    menuListView.setAdapter( listAdapter );  */
	}
	
	public boolean onCreateOptionsMenu(Menu m) {
		getMenuInflater().inflate(R.menu.menufeeds, m);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		switch (item.getItemId()) {
		case R.id.opciones:
			Intent loadAbout = new Intent(MenuNews.this, SetRss.class);
			startActivity(loadAbout);
			
			break;
		
		}
		return true;
	}
	
	public void onResume(){
 		super.onResume();
 		int back = prefs.getInt("background", R.drawable.mane6);
 		getWindow().setBackgroundDrawableResource(back);
 		
 		fntsDsp=fuentes.getInt("fuentesTotales", 0);
	    fntsCnf=fuentes.getInt("fuentesConfiguradas", 0);
	    index = 0;
	    
 		opciones = (ListaFeed[]) getLastNonConfigurationInstance() ;
 		
 		if ( opciones == null || opciones !=null) {
	    	opciones = new ListaFeed[fntsCnf];
	    	System.out.println("sdfadsfads");
	    	for(int i = 0; i<fntsDsp;i++)
	    	{
	    		System.out.println("BLEH");
	    		System.out.println(fuentes.getString("Name"+i, "none")+" i= "+ i);
	    		feed = fuentes.getString("Name"+i, "none");
	    		if(fuentes.getBoolean("Show"+feed, false))
	    		{
	    			opciones[index]=new ListaFeed(feed);
	    			index++;
	    		}
	  	      	
	    	}
	    	 
	    }
	    ArrayList<ListaFeed> menuList = new ArrayList<ListaFeed>();
	    menuList.addAll( Arrays.asList(opciones) );
	    
	    // Set our custom array adapter as the ListView's adapter.
	    listAdapter = new MenuNewsAdapter(this, menuList);
	   
	    menuListView.setAdapter( listAdapter );  
 		
 		
 	}
	
	
	
	private static class ListaFeed {
	    private String name = "" ;
	    public ListaFeed() {}
	    public ListaFeed( String name ) {
	      this.name = name ;
	    }
	    public ListaFeed( String name, boolean checked ) {
	      this.name = name ;
	    }
	    public String getName() {
	      return name;
	    }
	    public void setName(String name) {
	      this.name = name;
	    }
	    
	   
	    public String toString() {
	      return name ; 
	    }
	    
	  }
	  
	  /** Holds child views for one row. */
	  private static class MenuViewHolder {
	    private TextView textView ;
	    
	    public MenuViewHolder() {}
	    public MenuViewHolder( TextView textView) {
	      this.textView = textView ;
	    }
	    public TextView getTextView() {
	      return textView;
	    }
	    public void setTextView(TextView textView) {
	      this.textView = textView;
	    }    
	  }
	  
	  /** Custom adapter for displaying an array of Planet objects. */
	  private class MenuNewsAdapter extends ArrayAdapter<ListaFeed> {
	    
	    private LayoutInflater inflater;
	   
	    
	    public MenuNewsAdapter( Context context, List<ListaFeed> feedList ) {
	      super( context, R.layout.mlh_feeds_menunewsadapter, R.id.menuLista, feedList );
	      // Cache the LayoutInflate to avoid asking for a new one each time.
	      inflater = LayoutInflater.from(context) ;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	      // Planet to display
	      ListaFeed op = (ListaFeed) this.getItem( position ); 

	      // The child views in each row.
	       
	      TextView textView ; 
	      
	      // Create a new row view
	   //   if ( convertView == null ) {
	        convertView = inflater.inflate(R.layout.mlh_feeds_menunewsadapter, null);
	        textView = (TextView) convertView.findViewById( R.id.menuLista );
	   //   }
	     // else {
	    //    MenuViewHolder viewHolder = (MenuViewHolder) convertView.getTag();
	    //    textView = viewHolder.getTextView() ;
	    //  }
	       System.out.println("op.getName: "+op.getName());
	      textView.setText( op.getName() ); 
	      return convertView;
	    }
	    
	  }
	  
	  public Object onRetainNonConfigurationInstance() {
	    return opciones ;
	  }
}