package net.MLPHUB.teamMLH.Episodes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.MLPHUB.teamMLH.R;
import net.MLPHUB.teamMLH.R.drawable;
import net.MLPHUB.teamMLH.R.id;
import net.MLPHUB.teamMLH.R.layout;

import java.util.ArrayList;

public class EpisodeListAdapter extends ArrayAdapter<Episode> {
	ArrayList<Episode> _episodes;
	Context _context;
	

	public EpisodeListAdapter(Context context, int resourceId,
			ArrayList<Episode> episodes) {
		super(context, resourceId, episodes);
		this._episodes = episodes;
		this._context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null||!(convertView.getTag() instanceof ViewHolder)) {
			
			LayoutInflater inflater = LayoutInflater.from(_context);
					//.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.mlh_episodes_episodelistadapter, null);
		}

		Episode episode = _episodes.get(position);
		ImageView iv = (ImageView)
				 convertView.findViewById(R.id.thumbImageEpi);
		
		if (episode != null) {
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.epiTitle);
			holder.numEp = (TextView) convertView.findViewById(R.id.numEp);
			holder.numSeason = (TextView) convertView.findViewById(R.id.numSeason);
			
			convertView.setTag(holder);
		}
		else
		{
			convertView.setTag(holder);
		}
				
			holder.title.setText(episode.get_title());
			holder.numEp.setText(""+episode.get_number());
			holder.numSeason.setText(""+episode.get_season());
			
			if (iv != null) { 
				iv.setImageResource(R.drawable.nopicture);
				 iv.setImageDrawable(episode.getThumbImg()); 
			}
            	
            //holder.thumb.setImageBitmap(episode.getThumbImg());
    		
            
			
		
		return convertView;
	}
	
	private class ViewHolder {
        
        //ImageView thumb;
        TextView title;
        TextView numEp;
        TextView numSeason;
    }	
	
	

}
