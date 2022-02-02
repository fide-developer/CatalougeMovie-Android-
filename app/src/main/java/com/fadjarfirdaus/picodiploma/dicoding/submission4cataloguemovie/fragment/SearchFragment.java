package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.BuildConfig;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.ItemClickSupport;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.MovieDetail;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.adapter.MovieAdapter;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.modelview.ModelViewSearch;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.MovieDetail.EXTRAID;

public class SearchFragment extends Fragment {
    public static final String EXTRA_QUERY = "extra_query";
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    @BindView(R.id.rv_search)RecyclerView recyclerView;
    MovieAdapter adapter;
    private ArrayList<Model_Movie> movieArrayList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,getActivity());

        Bundle bundle = this.getArguments();
        String query = bundle.getString(EXTRA_QUERY);

        progressBar.setVisibility(View.VISIBLE);
        adapter = new MovieAdapter(getActivity());
        adapter.notifyDataSetChanged();

        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ModelViewSearch model = ViewModelProviders.of(this).get(ModelViewSearch.class);
        model.getMovie(query,getResources().getString(R.string.lang)).observe(this, new Observer<ArrayList<Model_Movie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Model_Movie> model_movies) {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
                adapter.setMovieArrayList(model_movies);
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
                                    moveToDetail.putExtra(EXTRAID,id);
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
        });
    }
}
