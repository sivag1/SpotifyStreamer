package com.sivag1.spotifystreamer.ui;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sivag1.spotifystreamer.R;
import com.sivag1.spotifystreamer.data.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MediaPlayerActivityFragment extends DialogFragment implements View.OnClickListener {

    TextView albumName, trackName, artistName;
    ImageView albumImage;
    ImageButton btnPrevious, btnPlay, btnNext;
    SeekBar seekBar;

    static int position = 0;
    ArrayList<Track> listOfTracks;

    static MediaPlayer mPlayer;
    View view;

    public MediaPlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_player, container, false);

        Bundle bundle = getArguments();
        if (savedInstanceState == null)
            position = bundle.getInt("position");
        listOfTracks = bundle.getParcelableArrayList("tracks");

        albumName = (TextView) view.findViewById(R.id.albumName);
        trackName = (TextView) view.findViewById(R.id.trackName);
        artistName = (TextView) view.findViewById(R.id.artistName);
        albumImage = (ImageView) view.findViewById(R.id.albumImage);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnPrevious = (ImageButton) view.findViewById(R.id.btnPrevious);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);

        btnPlay.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && fromUser) {
                    mPlayer.seekTo(progress * 1000);
                }
            }
        });

        int progress = 0;
        boolean isPlaying = true;
        if (savedInstanceState != null) {
            progress = savedInstanceState.getInt("progress");
            isPlaying = savedInstanceState.getBoolean("isPlaying");

            if (!isPlaying)
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
        }

        playPosition(position, progress, isPlaying);
        return view;
    }

    private void playPosition(int position, int progress, boolean isPlaying) {
        Track track = listOfTracks.get(position);

        albumName.setText(track.getAlbum());
        artistName.setText(track.getArtistName());
        trackName.setText(track.getName());

        Picasso.with(getActivity()).load(track.getImageURI()).into(albumImage);


        try {
            mPlayer.setDataSource(track.getPreviewURL());
            mPlayer.prepare();
            seekBar.setMax(mPlayer.getDuration() / 1000);
            mPlayer.seekTo(progress);
            seekBar.setProgress(progress / 1000);
            if (isPlaying)
                mPlayer.start();
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);

            final Handler mHandler = new Handler();
            //Make sure to update Seekbar on UI thread
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mPlayer != null) {
                        int mCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getActivity().getString(R.string.playback_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (position == listOfTracks.size() - 1) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.no_further), Toast.LENGTH_SHORT).show();
                } else {
                    mPlayer.reset();
                    playPosition(++position, 0, true);
                }
                break;
            case R.id.btnPrevious:
                if (position == 0) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.no_further), Toast.LENGTH_SHORT).show();
                } else {
                    mPlayer.reset();
                    playPosition(--position, 0, true);
                }
                break;
            default:
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    mPlayer.start();
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null)
            mPlayer.reset();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlayer != null) {
            outState.putInt("progress", mPlayer.getCurrentPosition());
            outState.putBoolean("isPlaying", mPlayer.isPlaying());
            mPlayer.stop();
            mPlayer.reset();
        }
    }
}
