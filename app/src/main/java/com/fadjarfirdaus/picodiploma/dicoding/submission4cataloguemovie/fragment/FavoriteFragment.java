package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.BuildConfig;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.ItemClickSupport;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.LoadCallBack;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.MovieDetail;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.adapter.MovieAdapter;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.MovieDetail.EXTRAID;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.database.DatabaseContract.MovieFavoriteColumn.CONTENT_URI;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.helper.MappingHelper.mapCursorToArrayList;

public class FavoriteFragment extends Fragment implements LoadCallBack {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rv_favorite)
    RecyclerView recyclerView;

    private MovieAdapter adapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static HandlerThread handlerThread;
    private DataObserver myObserver;
    ArrayList<Model_Movie> list = new ArrayList<>();
    LoadCallBack callBack = this;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,getActivity());
        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler,getContext(),this);
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        adapter = new MovieAdapter(getActivity());

        if (savedInstanceState == null) {
            new LoadNoteAsync(getContext(), this).execute();
        } else {
            list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                adapter.setMovieArrayList(list);
                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        final long id = adapter.getItemId(position);
                        AsyncHttpClient client = new AsyncHttpClient();
                        String url = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+ BuildConfig.TMDB_API_KEY+"&language="+getResources().getString(R.string.lang);

                        client.get(url, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    String result = new String(responseBody);
                                    JSONObject object = new JSONObject(result);

                                    int ids = object.getInt("id");
                                    String runtime = object.getString("runtime");
                                    String title = object.getString("title");
                                    String overview = object.getString("overview");
                                    String posterpath = object.getString("poster_path");
                                    String tagLine = object.getString("tagline");
                                    Double rating = object.getDouble("vote_average");
                                    String releaseDate= object.getString("release_date");
                                    String language = object.getString("original_language");
                                    String genre = "";
                                    JSONArray arrayGenre = object.getJSONArray("genres");
                                    if(arrayGenre.length()>1){
                                        genre = arrayGenre.getJSONObject(0).getString("name");

                                        for(int i = 1;i<arrayGenre.length();i++){
                                            JSONObject objGenre = arrayGenre.getJSONObject(i);
                                            String nextGen = objGenre.getString("name");

                                            genre = genre+", "+nextGen;
                                        }
                                    }else{
                                        genre = "no genre";
                                    }

                                    Model_Movie msg = new Model_Movie();
                                    msg.setId(ids);
                                    msg.setLanguage(language);
                                    msg.setOverview(overview);
                                    msg.setRuntime(runtime);
                                    msg.setGenres(genre);
                                    msg.setTitle(title);
                                    msg.setPosterpath(posterpath);
                                    msg.setRating(String.valueOf(rating));
                                    msg.setTagline(tagLine);
                                    msg.setReleasedate(releaseDate);

                                    Intent moveToDetail = new Intent(getActivity(), MovieDetail.class);
                                    moveToDetail.putExtra(MovieDetail.EXTRAS_DETAIL, msg);
                                    startActivity(moveToDetail);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void preExecute() {
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(Cursor movie) {
        progressBar.setVisibility(View.INVISIBLE);

        list = mapCursorToArrayList(movie);
        if (list.size() > 0) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            adapter.setMovieArrayList(list);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    final long id = adapter.getItemId(position);

                    AsyncHttpClient client = new AsyncHttpClient();
                    String url = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+ BuildConfig.TMDB_API_KEY+"&language="+getResources().getString(R.string.lang);
                    Log.d("ID : ", String.valueOf(id));
                    Log.d("URL : ", url);
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String result = new String(responseBody);
                                JSONObject object = new JSONObject(result);

                                int ids = object.getInt("id");
                                String runtime = object.getString("runtime");
                                String title = object.getString("title");
                                String overview = object.getString("overview");
                                String posterpath = object.getString("poster_path");
                                String tagLine = object.getString("tagline");
                                Double rating = object.getDouble("vote_average");
                                String releaseDate= object.getString("release_date");
                                String language = object.getString("original_language");
                                String genre = "";
                                JSONArray arrayGenre = object.getJSONArray("genres");
                                if(arrayGenre.length()>1){
                                    genre = arrayGenre.getJSONObject(0).getString("name");

                                    for(int i = 1;i<arrayGenre.length();i++){
                                        JSONObject objGenre = arrayGenre.getJSONObject(i);
                                        String nextGen = objGenre.getString("name");

                                        genre = genre+", "+nextGen;
                                    }
                                }else{
                                    genre = "no genre";
                                }

                                Model_Movie msg = new Model_Movie();
                                msg.setId(ids);
                                msg.setLanguage(language);
                                msg.setOverview(overview);
                                msg.setRuntime(runtime);
                                msg.setGenres(genre);
                                msg.setTitle(title);
                                msg.setPosterpath(posterpath);
                                msg.setRating(String.valueOf(rating));
                                msg.setTagline(tagLine);
                                msg.setReleasedate(releaseDate);

                                Intent moveToDetail = new Intent(getActivity(), MovieDetail.class);
                                moveToDetail.putExtra(EXTRAID,id);
                                moveToDetail.putExtra(MovieDetail.EXTRAS_DETAIL, msg);
                                startActivity(moveToDetail);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }
            });
        } else {
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        final Fragment fragment;
        public DataObserver(Handler handler, Context context,Fragment fragment) {
            super(handler);
            this.fragment=fragment;
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNoteAsync(context, (LoadCallBack) fragment).execute();
        }
    }
    public static class LoadNoteAsync extends AsyncTask<Void,Void,Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallBack> weakCallBack;

        private LoadNoteAsync(Context context, LoadCallBack callBack){
            weakContext = new WeakReference<>(context);
            weakCallBack = new WeakReference<>(callBack);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallBack.get().preExecute();
        }
        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return (context.getContentResolver().query(CONTENT_URI, null, null, null, null));
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            weakCallBack.get().postExecute(notes);
        }
    }
    private void showSnackbarMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,list);
    }
}
