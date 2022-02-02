package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract;

import static android.provider.BaseColumns._ID;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.getColumnInt;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.getColumnString;

public class Model_Movie implements Parcelable {
    String title,overview,tagline,releasedate,genres,posterpath,runtime,language;
    String rating;
    long id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.tagline);
        dest.writeString(this.releasedate);
        dest.writeString(this.genres);
        dest.writeString(this.posterpath);
        dest.writeString(this.runtime);
        dest.writeString(this.language);
        dest.writeValue(this.rating);
        dest.writeLong(this.id);
    }

    public Model_Movie() {
    }

    protected Model_Movie(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.tagline = in.readString();
        this.releasedate = in.readString();
        this.genres = in.readString();
        this.posterpath = in.readString();
        this.runtime = in.readString();
        this.language = in.readString();
        this.rating = in.readString();
        this.id = in.readLong();
    }
    public Model_Movie(long id, String title, String overview, String tagline, String releasedate, String genres, String posterpath,String runtime,String language, String rating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.tagline = tagline;
        this.releasedate = releasedate;
        this.genres = genres;
        this.posterpath = posterpath;
        this.runtime = runtime;
        this.language = language;
        this.rating = rating;
    }
    public Model_Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.overview = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.tagline = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.releasedate = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.genres = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.posterpath = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.runtime = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.language = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
        this.rating = getColumnString(cursor, DatabaseContract.MovieFavoriteColumn.TITLE);
    }
    public static final Parcelable.Creator<Model_Movie> CREATOR = new Parcelable.Creator<Model_Movie>() {
        @Override
        public Model_Movie createFromParcel(Parcel source) {
            return new Model_Movie(source);
        }

        @Override
        public Model_Movie[] newArray(int size) {
            return new Model_Movie[size];
        }
    };
}
