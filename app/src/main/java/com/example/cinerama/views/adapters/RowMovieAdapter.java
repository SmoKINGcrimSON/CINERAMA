package com.example.cinerama.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cinerama.R;
import com.example.cinerama.models.Movie;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.fragments.MovieFragment;
import java.util.ArrayList;

public class RowMovieAdapter extends RecyclerView.Adapter<RowMovieAdapter.RowMovieViewHolder> {
    Context context;
    ArrayList<Movie> movies;
    public RowMovieAdapter(Context context, ArrayList<Movie> movies){
        this.context = context;
        this.movies = movies;
    }
    @NonNull
    @Override
    public RowMovieAdapter.RowMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.movie_row, parent, false);
        return new RowMovieAdapter.RowMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowMovieAdapter.RowMovieViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(movies.get(position).getPoster())
                .placeholder(R.drawable.image_broke)
                .error(R.drawable.image_broke)
                .into(holder.movie_poster);
        holder.movie_title.setText(movies.get(position).getTitle());
        holder.itemView.setOnClickListener(e -> Tools.genFragment((AppCompatActivity) context, MovieFragment.newInstance(movies.get(position)), movies.get(position), R.id.frame_layout));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public static class RowMovieViewHolder extends RecyclerView.ViewHolder{
        ImageView movie_poster;
        TextView movie_title;
        public RowMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movie_poster = itemView.findViewById(R.id.movie_poster);
            movie_title = itemView.findViewById(R.id.movie_title);
        }
    }
}
