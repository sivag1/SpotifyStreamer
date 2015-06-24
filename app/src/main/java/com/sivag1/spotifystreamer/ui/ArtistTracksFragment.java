package com.sivag1.spotifystreamer.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.sivag1.spotifystreamer.R;
import com.sivag1.spotifystreamer.Utils;
import com.sivag1.spotifystreamer.data.TrackAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.RetrofitError;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistTracksFragment extends Fragment {

    private AbsListView mListView;
    private ArrayAdapter<com.sivag1.spotifystreamer.data.Track> mAdapter;
    private ArrayList<com.sivag1.spotifystreamer.data.Track> listOfTracks;

    private static String spotifyId = null;
    private static String artistName = "";

    public ArtistTracksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_tracks, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.sivag1.spotifystreamer.data.Track track = listOfTracks.get(position);

                boolean mIsLargeLayout = getResources().getBoolean(R.bool.isTablet);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                DialogFragment newFragment = new MediaPlayerActivityFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putParcelableArrayList("tracks", listOfTracks);
                newFragment.setArguments(bundle);

                if (mIsLargeLayout) {
                    // The device is using a large layout, so show the fragment as a dialog
                    newFragment.show(fragmentManager, "dialog");
                } else {
//                    // The device is smaller, so show the fragment fullscreen
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    // For a little polish, specify a transition animation
//                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    // To make it fullscreen, use the 'content' root view as the container
//                    // for the fragment, which is always the root view for the activity
//                    transaction.replace(android.R.id.content, newFragment)
//                            .addToBackStack(null).commit();

                    Intent i = new Intent(getActivity(), MediaPlayerActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        });

        if (savedInstanceState != null)
            loadList();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tempStr = getArguments().getString("SpotifyId");

        if (tempStr != null) {
            spotifyId = tempStr;
        }

        tempStr = getArguments().getString("ArtistName");

        if (tempStr != null) {
            artistName = tempStr;
        }

        if (savedInstanceState == null) {
            if (Utils.isNetworkAvailable(getActivity()))
                new ArtistTracksPullTask().execute(spotifyId);
            else
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            listOfTracks = savedInstanceState.getParcelableArrayList("tracks");
        }
    }

    private class ArtistTracksPullTask extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map map = new HashMap();
            map.put("country", "US");
            ArrayList<com.sivag1.spotifystreamer.data.Track> list = new ArrayList<com.sivag1.spotifystreamer.data.Track>();

            try {
                List<Track> webList = spotify.getArtistTopTrack(params[0], map).tracks;

                for (kaaes.spotify.webapi.android.models.Track item : webList) {
                    com.sivag1.spotifystreamer.data.Track track = new com.sivag1.spotifystreamer.data.Track();
                    track.setName(item.name);
                    track.setAlbum(item.album.name);
                    track.setPreviewURL(item.preview_url);
                    track.setArtistName(artistName);
                    List<Image> listImage = item.album.images;

                    if (listImage.size() > 0)
                        track.setImageURI(item.album.images.get(0).url);
                    list.add(track);
                }
            } catch (RetrofitError error) {
                // Do nothing, empty list will be returned
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);

            listOfTracks = list;

            loadList();
        }
    }

    private void loadList() {
        if (getActivity() == null) {
            // Some weird error
        } else {
            mAdapter = new TrackAdapter(getActivity(), listOfTracks);
            mListView.setAdapter(mAdapter);

            if (listOfTracks.size() == 0)
                Toast.makeText(getActivity(), getActivity().getString(R.string.track_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("tracks", listOfTracks);
    }
}
