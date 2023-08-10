package net.MLPHUB.teamMLH.Feeds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.text.SpannedString;

public class Feed {
	
	private String title;
	private Spanned desc;
	private String link;
	private String date;
	private String imageLink;
	private Drawable img;
	private String htmlText;
	private String dc;
	
	public Feed()
	{
		title = "";
		desc = new SpannedString("");
		link = "";
		date = "";
		imageLink = "";
	}
	
	public Feed(String t, String dt, Spanned d, String l, String il)
	{
		this.title = t;
		this.date = dt;
		this.desc = d;
		this.link = l;
		this.imageLink = il;	
	}
	
	public void loadImagen(String url) throws MalformedURLException, IOException
	{
		InputStream is = (InputStream) new URL(url).getContent();
		img = Drawable.createFromStream(is, "src");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Spanned getDesc() {
		return desc;
	}

	public void setDesc(Spanned desc) {
		this.desc = desc;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public Drawable getImg() {
		return img;
	}

	public void setImg(Drawable img) {
		this.img = img;
	}

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}
	
	

}
