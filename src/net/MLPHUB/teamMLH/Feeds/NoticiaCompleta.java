package net.MLPHUB.teamMLH.Feeds;


import net.MLPHUB.teamMLH.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class NoticiaCompleta extends Activity {
    /** Called when the activity is first created. */
       
    private String titulo, hFeed, htmlText, URL;
    private TextView title;
    private WebView feed;
    private Button menu;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mlh_feeds_noticiacompleta);
        Bundle extras = getIntent().getExtras();
        
        if(extras!=null)
		{
			titulo = extras.getString("Titulo");
			htmlText = extras.getString("HtmlText");
			hFeed = extras.getString("Hashtag");
			URL = extras.getString("URL");
			
									
		}
        
        
      
        String html = "<html>"
        		+ "<head>"
                + "<style type=\"text/css\">body{color: #fff; background-color: #000;}"
                + "</style>"
               // + "<script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js'></script><script type='text/javascript' src='http://ponycountdown.com/api.js'></script>"
        		+ "</head>"
                + "<body><div>"                          
                + htmlText
                //+ "<script type=\"text/javascript\">ponyCountdown();</script>"
                + "</div></body></html>";
        
        menu = (Button)findViewById(R.id.menu);
        title = (TextView)findViewById(R.id.newsTitle);
        feed =(WebView)findViewById(R.id.feed);
        title.setText(titulo);
        title.setSelected(true);
        
        feed.getSettings().setJavaScriptEnabled(true); 
        feed.setWebViewClient(null);
        feed.getSettings().setLoadWithOverviewMode(true);
        
        feed.loadDataWithBaseURL(null, html, "text/html", "utf-8","about:blank");
      	     	
    }
    
    public void onDestroy()
    {    	
    	super.onDestroy();
    }
    public void onBackPressed()
    {
    	super.onBackPressed();
    	finish();
    }
    
    public void goUrl(View v)
    {
    	Intent goAction = new Intent(Intent.ACTION_VIEW, Uri
				.parse(URL));
    	startActivity(goAction);
    }
    
    public void share(View v)
    {
    	Intent shareAction = new Intent(Intent.ACTION_SEND);
	    shareAction.setType("text/plain");
	    shareAction.putExtra(Intent.EXTRA_TEXT, titulo+" "+getString(R.string.via)+" "+ hFeed +" "+
	    						getString(R.string.from)+" #MyLittleHub "+URL);
	    startActivity(Intent.createChooser(shareAction, getString(R.string.share)));
    }
}