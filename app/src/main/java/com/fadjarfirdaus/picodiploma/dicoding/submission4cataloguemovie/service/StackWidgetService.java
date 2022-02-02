package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.adapter.StackRemoteViewFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewFactory(this.getApplicationContext());
    }
}
