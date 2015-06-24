package com.sivag1.spotifystreamer.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sivag1 on 6/17/15.
 */
public class Track implements Parcelable {

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    private String imageURI;
    private String name;
    private String album;
    private String previewURL;
    private String artistName;

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(album);
        dest.writeString(imageURI);
        dest.writeString(previewURL);
        dest.writeString(artistName);
    }

    public Track() {

    }

    private Track(Parcel in) {
        this.name = in.readString();
        this.album = in.readString();
        this.imageURI = in.readString();
        this.previewURL = in.readString();
        this.artistName = in.readString();
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {

        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
