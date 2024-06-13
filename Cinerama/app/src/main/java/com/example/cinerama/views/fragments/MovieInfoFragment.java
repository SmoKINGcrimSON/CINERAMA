package com.example.cinerama.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.cinerama.R;
import com.example.cinerama.models.Movie;

public class MovieInfoFragment extends Fragment {
    private static final String ARG_MOVIE = "movie";
    private Movie movie;
    public static MovieInfoFragment newInstance(Movie movie) {
        MovieInfoFragment fragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) movie = (Movie) getArguments().getSerializable(ARG_MOVIE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.movie_plot)).setText(movie.getPlot());
        ((TextView) view.findViewById(R.id.movie_director)).setText(movie.getDirector());
        ((TextView) view.findViewById(R.id.movie_cast)).setText(movie.getActors());
        ((TextView) view.findViewById(R.id.movie_idiomas)).setText(movie.getLanguage());
        ImageView movie_poster = view.findViewById(R.id.movie_poster);
        Glide.with(movie_poster)
                .load(movie.getPoster())
                .placeholder(R.drawable.image_broke)
                .error(R.drawable.image_broke)
                .into(movie_poster);
    }
}