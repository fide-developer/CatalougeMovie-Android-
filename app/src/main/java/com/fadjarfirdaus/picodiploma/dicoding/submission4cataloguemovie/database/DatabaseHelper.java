package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbmoviecatalogue";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"+
                    "(%s INTEGER PRIMARY KEY,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL,"+
                    "%s TEXT NOT NULL)",
            TABLE_NAME,
            DatabaseContract.MovieFavoriteColumn._ID,
            DatabaseContract.MovieFavoriteColumn.TITLE,
            DatabaseContract.MovieFavoriteColumn.TAGLINE,
            DatabaseContract.MovieFavoriteColumn.RATING,
            DatabaseContract.MovieFavoriteColumn.DURATION,
            DatabaseContract.MovieFavoriteColumn.LANGUAGE,
            DatabaseContract.MovieFavoriteColumn.GENRE,
            DatabaseContract.MovieFavoriteColumn.RELEASE_DATE,
            DatabaseContract.MovieFavoriteColumn.OVERVIEW,
            DatabaseContract.MovieFavoriteColumn.POSTER);

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
