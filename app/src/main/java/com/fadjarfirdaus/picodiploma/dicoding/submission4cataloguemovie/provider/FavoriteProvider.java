package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.FavoriteHelper;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment.FavoriteFragment;

import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.AUTHORITY;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.CONTENT_URI;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TABLE_NAME;

public class FavoriteProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favHelper;

    static {
        sUriMatcher.addURI(AUTHORITY,TABLE_NAME,MOVIE);
        sUriMatcher.addURI(AUTHORITY,TABLE_NAME+"/#",MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        favHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,@Nullable String[] projection,@Nullable String selection,@Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = favHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,@Nullable ContentValues values) {
        favHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = favHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteFragment.DataObserver(new Handler(), getContext(),new FavoriteFragment()));
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = favHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteFragment.DataObserver(new Handler(), getContext(),new FavoriteFragment()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri,@Nullable ContentValues values,@Nullable String selection,@Nullable String[] selectionArgs) {
        return 0;
    }
}
