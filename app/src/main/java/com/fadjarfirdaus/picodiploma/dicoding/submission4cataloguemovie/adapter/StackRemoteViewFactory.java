package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.FavoritWidget;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.FavoriteHelper;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Bitmap> mWidgetItems;
    ArrayList<Model_Movie> listFav = new ArrayList<>();
    private final Context mContext;

    public StackRemoteViewFactory(Context context) {
        this.mContext = context;
    }
    private FavoriteHelper helper;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        helper = new FavoriteHelper(mContext);
        mWidgetItems = new ArrayList<>();
        helper.open();
        loadData();
        helper.close();
    }

    private void loadData() {
        listFav = helper.getFavorit();

        for (int i=0;i<listFav.size();i++){

            try {
            FutureTarget<Bitmap> futureBitmap = Glide.with(mContext)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w185"+listFav.get(i).getPosterpath())
                    .submit();

                Bitmap myBitmap = futureBitmap.get();
                mWidgetItems.add(myBitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("TESTIMAGE", String.valueOf(mWidgetItems.size()));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageWidget, mWidgetItems.get(position));


        Bundle extras = new Bundle();
        extras.putInt(FavoritWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageWidget, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
