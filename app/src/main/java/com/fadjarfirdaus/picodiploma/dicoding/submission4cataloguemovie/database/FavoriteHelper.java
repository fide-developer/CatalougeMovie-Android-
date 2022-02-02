package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.POSTER;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TABLE_NAME;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TITLE;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }
    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();
        if(database.isOpen()) database.close();
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }
    public ArrayList<Model_Movie> getFavorit() {
        Cursor cursor = database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
        cursor.moveToFirst();
        ArrayList<Model_Movie> arrayList = new ArrayList<>();
        Model_Movie modelMovie;
        if (cursor.getCount() > 0) {
            do {
                modelMovie = new Model_Movie();
                modelMovie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                modelMovie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                modelMovie.setPosterpath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                arrayList.add(modelMovie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }
    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
