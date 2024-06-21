package com.example.cinerama.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.cinerama.R;
import com.example.cinerama.models.Movie;
import com.example.cinerama.utils.Tools;

public class MovieFragment extends Fragment {
    private static final String ARG_MOVIE = "movie";
    private Movie movie;
    private Button currentButton = null;
    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();
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
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ////START
        ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_movie, MovieInfoFragment.newInstance(movie))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        Tools.changeButtonColor(currentButton, view.findViewById(R.id.btn_info));
        currentButton = view.findViewById(R.id.btn_info);
        ////BUTTONS
        Button btn_info = view.findViewById(R.id.btn_info);
        Button btn_comprar = view.findViewById(R.id.btn_comprar);
        ////SET TEXT
        ((TextView) view.findViewById(R.id.movie_title)).setText(movie.getTitle());
        ((TextView) view.findViewById(R.id.movie_info)).setText(movie.getGenre()
                .toString().substring(1, movie.getGenre().toString().length() - 1) + "|" + movie.getDuration() + " min|" + movie.getRated());
        ///EVENTS
        btn_info.setOnClickListener(e -> {
            Tools.genFragment((AppCompatActivity) getContext(), MovieInfoFragment.newInstance(movie), movie, R.id.frame_layout_movie);
            Tools.changeButtonColor(currentButton, btn_info);
            currentButton = btn_info;
        });
        btn_comprar.setOnClickListener(e -> {
            Tools.genFragment((AppCompatActivity) getContext(), ProyeccionesFragment.newInstance(movie.getId()), movie.getId(), R.id.frame_layout_movie);
            Tools.changeButtonColor(currentButton, btn_comprar);
            currentButton = btn_comprar;
        });
    }
}