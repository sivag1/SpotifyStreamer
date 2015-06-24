package com.sivag1.spotifystreamer.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sivag1.spotifystreamer.R;


public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callbacks {

    boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null)
            isTwoPane = true;
        else
            isTwoPane = false;

        if (savedInstanceState == null) {
            Fragment fragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentAlbums, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String id, String name) {
        if (isTwoPane) {
            Fragment fragment = new ArtistTracksFragment();
            Bundle arguments = new Bundle();
            arguments.putString("SpotifyId", id);
            arguments.putString("ArtistName", name);
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, ArtistTracksActivity.class);
            detailIntent.putExtra("SpotifyId", id);
            detailIntent.putExtra("ArtistName", name);
            startActivity(detailIntent);
        }
    }
}
