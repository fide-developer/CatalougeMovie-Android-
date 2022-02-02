package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.modelview;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.BuildConfig;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ModelViewSearch extends ViewModel {
    private MutableLiveData<ArrayList<Model_Movie>> list;

    public LiveData<ArrayList<Model_Movie>> getMovie(String query,String lang){
        if (list == null) {
            list = new MutableLiveData<ArrayList<Model_Movie>>();
            loadMovies(query,lang);
        }

        return list;
    }

    private void loadMovies(String query,String lang) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/search/movie?api_key="+BuildConfig.TMDB_API_KEY+"&language="+lang+"&query="+query;
        final ArrayList<Model_Movie> arrayLists = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Model_Movie movie = null;
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray responseArray = responseObject.getJSONArray("results");

                    for (int i = 0;i<responseArray.length();i++){
                        movie = new Model_Movie();
                        movie.setTitle(responseArray.getJSONObject(i).getString("title"));
                        String overV = responseArray.getJSONObject(i).getString("overview");
                        if(TextUtils.isEmpty(overV)){
                            movie.setOverview("-");
                        }else{
                            movie.setOverview(overV);
                        }
                        movie.setId(responseArray.getJSONObject(i).getInt("id"));
                        movie.setPosterpath(responseArray.getJSONObject(i).getString("poster_path"));

                        arrayLists.add(movie);
                        list.setValue(arrayLists);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
