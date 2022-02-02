package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.FavoriteHelper;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.BaseColumns._ID;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.CONTENT_URI;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.DURATION;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.GENRE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.LANGUAGE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.OVERVIEW;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.POSTER;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.RATING;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.RELEASE_DATE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TAGLINE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.TITLE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.helper.MappingHelper.mapCursorToArrayList;

public class MovieDetail extends AppCompatActivity {
    public static final String EXTRAS_DETAIL = "extras_detail";
    public static final String EXTRAID = "extraid";
    @BindView(R.id.button_favorite)
    ImageButton button_favorite;
    @BindView(R.id.circle_poster)
    CircleImageView poster;

    @BindView(R.id.textTitle_genre) TextView titleGenre;
    @BindView(R.id.textTitle_language) TextView titleLanguage;
    @BindView(R.id.textTitle_overview) TextView titleOveriew;
    @BindView(R.id.textTitle_rateFilm)TextView titleRate;
    @BindView(R.id.textTitle_releaseDate) TextView titleRelease;
    @BindView(R.id.textTitle_runtime) TextView titleRuntime;

    @BindView(R.id.text_overview) TextView textOverview;
    @BindView(R.id.text_genre) TextView textGenre;
    @BindView(R.id.text_language) TextView textLanguage;
    @BindView(R.id.text_rateFilm) TextView textRate;
    @BindView(R.id.text_releaseDate) TextView textRelease;
    @BindView(R.id.text_runtime) TextView textRuntime;
    @BindView(R.id.text_TagLine) TextView textTagline;
    @BindView(R.id.text_Title) TextView textTitle;

    Cursor cursor;
    Model_Movie detData;
    Uri uri;
    FavoriteHelper favoriteHelper;
    ArrayList<Model_Movie> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        detData = new Model_Movie();
        detData = getIntent().getParcelableExtra(EXTRAS_DETAIL);
        long id = getIntent().getLongExtra(EXTRAID,0);
        Log.d("TESTKEDUA", String.valueOf(id));
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(detData.getTitle());
        }

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        titleGenre.setText(getResources().getString(R.string.titleGenre));
        titleLanguage.setText(getResources().getString(R.string.titleLang));
        titleOveriew.setText(getResources().getString(R.string.titleOverview));
        titleRate.setText(getResources().getString(R.string.titileRate));
        titleRelease.setText(getResources().getString(R.string.titleRelease));
        titleRuntime.setText(getResources().getString(R.string.titleRuntime));

        textGenre.setText(detData.getGenres());
        textLanguage.setText(detData.getLanguage());
        textOverview.setText(detData.getOverview());
        textRate.setText(String.valueOf(detData.getRating()));
        textRelease.setText(detData.getReleasedate());
        textRuntime.setText(detData.getRuntime());
        textTagline.setText(detData.getTagline());
        textTitle.setText(detData.getTitle());

        Glide.with(this).load("https://image.tmdb.org/t/p/w185/"+detData.getPosterpath()).into(poster);

        uri = Uri.parse(CONTENT_URI + "/" + id);
        cursor = getContentResolver().query(uri, null, null, null, null);
        list = mapCursorToArrayList(cursor);

        if (list.size() == 0){
            button_favorite.setImageResource(R.drawable.ic_favorite_unactive);
        }else {
            button_favorite.setImageResource(R.drawable.ic_favorite_active);
        }
    }
    @OnClick(R.id.button_favorite)
    public void favorite(View view){
        Intent intent = new Intent(this, FavoritWidget.class);
        intent.setAction(FavoritWidget.UPDATE_ACTION);

        int[] ids = AppWidgetManager.getInstance(this)
                .getAppWidgetIds(new ComponentName(this, FavoritWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        Log.d("Kenapa ieu : ",String.valueOf(list.size()));
        if (list.size() > 0){
            button_favorite.setImageResource(R.drawable.ic_favorite_unactive);
            getContentResolver().delete(uri, null, null);
        }else{
            long id = getIntent().getLongExtra(EXTRAID,0);
            button_favorite.setImageResource(R.drawable.ic_favorite_active);
            ContentValues values = new ContentValues();
            values.put(_ID, String.valueOf(id));
            values.put(TITLE,detData.getTitle());
            values.put(TAGLINE,detData.getTagline());
            values.put(RATING,detData.getRating());
            values.put(DURATION,detData.getRuntime());
            values.put(LANGUAGE,detData.getLanguage());
            values.put(GENRE,detData.getGenres());
            values.put(RELEASE_DATE,detData.getReleasedate());
            values.put(OVERVIEW,detData.getOverview());
            values.put(POSTER,detData.getPosterpath());
            getContentResolver().insert(CONTENT_URI, values);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
