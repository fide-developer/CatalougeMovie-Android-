package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.model.Model_Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
private Context context;
private ArrayList<Model_Movie> movieArrayList;

    public ArrayList<Model_Movie> getMovieArrayList() {
        return movieArrayList;
    }

    public void setMovieArrayList(ArrayList<Model_Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemrow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_movie,viewGroup,false);
        return new MovieViewHolder(itemrow);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int i) {
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185"+getMovieArrayList().get(i).getPosterpath())
                .apply(new RequestOptions().override(140,180))
                .into(holder.imgPoster);
        holder.title.setText(getMovieArrayList().get(i).getTitle());
        holder.overview.setText(getMovieArrayList().get(i).getOverview());
    }

    @Override
    public long getItemId(int position) {
        return getMovieArrayList().get(position).getId();
    }

    @Override
    public int getItemCount() {
        return getMovieArrayList().size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)TextView title;
        @BindView(R.id.text_overview)TextView overview;
        @BindView(R.id.img_poster)ImageView imgPoster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
