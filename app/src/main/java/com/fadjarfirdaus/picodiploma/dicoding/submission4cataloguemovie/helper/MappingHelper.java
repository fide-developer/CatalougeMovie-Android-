package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.helper;

import android.database.Cursor;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.DURATION;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.GENRE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.LANGUAGE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.OVERVIEW;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.POSTER;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.RATING;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.RELEASE_DATE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TAGLINE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TITLE;

public class MappingHelper {
    public static ArrayList<Model_Movie> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<Model_Movie> movieList = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String tagline = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TAGLINE));
            String overview = notesCursor.getString(notesCursor.getColumnIndexOrThrow(OVERVIEW));
            String genres = notesCursor.getString(notesCursor.getColumnIndexOrThrow(GENRE));
            String releasedate = notesCursor.getString(notesCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String rating = notesCursor.getString(notesCursor.getColumnIndexOrThrow(RATING));
            String runtime = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DURATION));
            String language = notesCursor.getString(notesCursor.getColumnIndexOrThrow(LANGUAGE));
            String posterpath = notesCursor.getString(notesCursor.getColumnIndexOrThrow(POSTER));
            movieList.add(new Model_Movie( id,title, overview, tagline, releasedate, genres, posterpath,runtime,language,rating));
        }
        return movieList;
    }
}
