package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie;

import android.database.Cursor;

public interface LoadCallBack {
    void preExecute();
    void postExecute(Cursor movie);
}

