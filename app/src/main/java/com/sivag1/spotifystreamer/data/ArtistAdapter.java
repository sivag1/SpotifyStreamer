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

public class ArtistAdapter extends ArrayAdapter<Artist> {
    private final Context context;
    private final List<Artist> values;

    public ArtistAdapter(Context context, List<Artist> values) {
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
            rowView = inflater.inflate(R.layout.artist_row_layout, parent, false);
        else
            rowView = convertView;

        Artist artist = values.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.name);
        textView.setText(artist.getName());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if (artist.getImageURI() != null)
            Picasso.with(context).load(artist.getImageURI()).into(imageView);

        return rowView;
    }
}

