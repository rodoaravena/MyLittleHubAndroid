package net.MLPHUB.teamMLH.Episodes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.text.SpannedString;

public class Episode {

	private String _title;
	private int _season;
	private int _number;
	private String _director;
	private String _writer;
	private String _picture;
	private String _image;
	private String _desc;
	private Spanned _synopsis;
	private Drawable downloadedImg;
	private Drawable thumbImg;
	private Bitmap image;
	
	public Episode() {
		_title = "";
		_season = 0;
		_number = 0;
		_director = "";
		_writer = "";
		_picture = "";
		_synopsis = new SpannedString("");
	}


	public Episode(String title, int season, int number, String director,
			String writer, String picture, Spanned synopsis) {
		this._title = title;
		this._number = number;
		this._season = season;
		this._director = director;
		this._writer = writer;
		this._picture = picture;
		this._synopsis = synopsis;
	}

	public void loadThumbImage(String url) throws MalformedURLException,
			IOException {
		InputStream is = (InputStream) new URL(url).getContent();
		thumbImg = Drawable.createFromStream(is, "src");
	}
	
	public void loadFullImage(String url) throws MalformedURLException,	IOException {
		InputStream is = (InputStream) new URL(url).getContent();
		downloadedImg = Drawable.createFromStream(is, "src");
	}

	public String get_title() {
		return _title;
	}
	
	public void set_title(String _title) {
		this._title = _title;
	}

	public int get_season() {
		return _season;
	}
	
	public void set_season(int _season){
		this._season=_season;
	}

	public int get_number() {
		return _number;
	}

	public void set_number(int _number) {
		this._number = _number;
	}

	public String get_director() {
		return _director;
	}

	public void set_director(String _director) {
		this._director = _director;
	}

	public String get_writer() {
		return _writer;
	}

	public void set_writer(String _writer) {
		this._writer = _writer;
	}

	public String get_picture() {
		return _picture;
	}

	public void set_picture(String _picture) {
		this._picture = _picture;
	}

	public String get_image() {
		return _image;
	}



	public void set_image(String _image) {
		this._image = _image;
	}



	public String get_desc() {
		return _desc;
	}


	public void set_desc(String _desc) {
		this._desc = _desc;
	}


	public Spanned get_synopsis() {
		return _synopsis;
	}

	public void set_synopsis(Spanned _synopsis) {
		this._synopsis = _synopsis;
	}


	public Drawable getThumbImg() {
		return thumbImg;
	}

	public void setThumbImg(Drawable thumbImg) {
		this.thumbImg = thumbImg;
	}


	public Bitmap getImage() {
		return image;
	}


	public void setImage(Bitmap image) {
		this.image = image;
	}


	public Drawable getDownloadedImg() {
		return downloadedImg;
	}


	public void setDownloadedImg(Drawable downloadedImg) {
		this.downloadedImg = downloadedImg;
	}


}
