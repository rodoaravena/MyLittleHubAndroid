package net.MLPHUB.teamMLH.Feeds;

import java.util.ArrayList;

import net.MLPHUB.teamMLH.R;
import net.MLPHUB.teamMLH.R.id;
import net.MLPHUB.teamMLH.R.layout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class NewsAdapter extends ArrayAdapter<Feed> {
	ArrayList<Feed> news;
	Context context;
	Bitmap bitmap;
	int flag;
	
	public NewsAdapter(Context context, int textViewResourceId,
			ArrayList<Feed> noticias) {
		super(context, textViewResourceId, noticias);
		this.news = noticias;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null||!(convertView.getTag() instanceof ViewHolder)) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.mlh_feeds_newsadapter, null);
		}

		Feed feed = news.get(position);
		if (feed != null) {
			holder = new ViewHolder();
			TextView date = (TextView) convertView.findViewById(R.id.RowDate);
			TextView title = (TextView) convertView.findViewById(R.id.RowTitle);
			TextView desc = (TextView) convertView
					.findViewById(R.id.RowContent);
			/*holder.img = (ImageView) convertView
					.findViewById(R.id.thumbImage);*/
			 ImageView iv = (ImageView)
			 convertView.findViewById(R.id.thumbImage);
			if (date != null) {
				date.setText(feed.getDate());
			}
			if (title != null) {
				title.setText(feed.getTitle());
			}
			if (desc != null) {
				desc.setText(feed.getDesc());
			}
			/*if (holder.img != null) {
				try {
					bitmap = BitmapFactory.decodeStream((InputStream)new URL(feed.getImageLink()).getContent());
					
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				holder.img.setImageBitmap(bitmap);
			}*/
			
			 if (iv != null) { 
			 iv.setImageDrawable(feed.getImg()); 
			 }
			 
		}
		return convertView;
	}
	
	private class ViewHolder {
        
        ImageView img;
    }
}