package com.sivag1.spotifystreamer.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.sivag1.spotifystreamer.R;
import com.sivag1.spotifystreamer.Utils;
import com.sivag1.spotifystreamer.data.Artist;
import com.sivag1.spotifystreamer.data.ArtistAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private AbsListView mListView;
    private EditText etext = null;
    private ArrayAdapter<Artist> mAdapter;
    private ArrayList<Artist> listOfArtists = new ArrayList<Artist>();
    private Callbacks mCallbacks;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        final CustomBool bool = new CustomBool();

        if (savedInstanceState != null)
            bool.isNew = false;
        else
            bool.isNew = true;

        etext = (EditText) view.findViewById(R.id.editTxt);

        mCallbacks = (Callbacks) getActivity();

        mListView = (AbsListView) view.findViewById(android.R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = listOfArtists.get(position);
                mCallbacks.onItemSelected(artist.getSpotityId(), artist.getName());
            }
        });

        etext = (EditText) view.findViewById(R.id.editTxt);
        etext.addTextChangedListener(new

                                             TextWatcher() {

                                                 @Override
                                                 public void onTextChanged(CharSequence s,
                                                                           int start, int before,
                                                                           int count) {
                                                 }

                                                 @Override
                                                 public void beforeTextChanged(CharSequence s,
                                                                               int start, int count,
                                                                               int after) {
                                                 }

                                                 @Override
                                                 public void afterTextChanged(Editable s) {
                                                     if (bool.isNew) {
                                                         if (s.toString().length() > 0) {
                                                             if (Utils.isNetworkAvailable(getActivity()))
                                                                 new ArtistPullTask().execute(s.toString());
                                                             else
                                                                 Toast.makeText(getActivity(), getActivity().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                                                         }
                                                     } else {
                                                         bool.isNew = false;
                                                         listOfArtists = savedInstanceState.getParcelableArrayList("artists");
                                                         loadArtists();
                                                     }
                                                 }
                                             }

        );

        return view;
    }

    private class ArtistPullTask extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            ArrayList<Artist> list = new ArrayList<Artist>();

            try {
                ArtistsPager results = spotify.searchArtists(params[0]);

                List<kaaes.spotify.webapi.android.models.Artist> webList = results.artists.items;

                for (kaaes.spotify.webapi.android.models.Artist item : webList) {
                    Artist artist = new Artist();
                    artist.setName(item.name);
                    artist.setSpotityId(item.id);
                    List<Image> listImage = item.images;

                    if (listImage.size() > 0)
                        artist.setImageURI(item.images.get(0).url);
                    list.add(artist);
                }
            } catch (RetrofitError error) {
                // Do nothing, empty list will be returned
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList list) {
            super.onPostExecute(list);

            listOfArtists = list;

            loadArtists();
        }
    }

    private void loadArtists() {
        if (getActivity() == null)
            Toast.makeText(getActivity(), getActivity().getString(R.string.artist_not_found), Toast.LENGTH_SHORT).show();
        else {
            mAdapter = new ArtistAdapter(getActivity(), listOfArtists);
            mListView.setAdapter(mAdapter);

            if (listOfArtists.size() == 0)
                Toast.makeText(getActivity(), getActivity().getString(R.string.artist_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id, String name);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("artists", listOfArtists);
    }

    class CustomBool {
        public boolean isNew;
    }
}
