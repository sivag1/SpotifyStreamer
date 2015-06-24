package com.sivag1.spotifystreamer.data;

/**
 * Created by sivag1 on 6/16/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sivag1.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {
    private final Context context;
    private final List<Track> values;

    public TrackAdapter(Context context, List<Track> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        if (convertView == null)
            rowView = inflater.inflate(R.layout.artist_tracks_row_layout, parent, false);
        else
            rowView = convertView;

        Track track = values.get(position);

        TextView trackName = (TextView) rowView.findViewById(R.id.track);
        trackName.setText(track.getName());

        TextView albumName = (TextView) rowView.findViewById(R.id.album);
        albumName.setText(track.getAlbum());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if (track.getImageURI() != null)
            Picasso.with(context).load(track.getImageURI()).into(imageView);

        return rowView;
    }
}

