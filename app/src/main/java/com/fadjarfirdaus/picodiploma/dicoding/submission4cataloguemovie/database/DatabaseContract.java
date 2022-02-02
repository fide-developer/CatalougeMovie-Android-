package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie";
    private static final String SCHEME = "content";

    public DatabaseContract() {
    }

    public static final class MovieFavoriteColumn implements BaseColumns{
        public static String TABLE_NAME = "movie_favorite";
        public static String TITLE = "title";
        public static String OVERVIEW = "overview";
        public static String TAGLINE = "tagline";
        public static String POSTER = "poster";
        public static String RATING = "rating";
        public static String LANGUAGE = "language";
        public static String DURATION = "duration";
        public static String GENRE = "genre";
        public static String RELEASE_DATE = "release_date";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
