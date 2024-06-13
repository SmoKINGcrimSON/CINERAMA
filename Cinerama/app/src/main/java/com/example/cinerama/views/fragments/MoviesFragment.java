package com.example.cinerama.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cinerama.R;
import com.example.cinerama.models.Movie;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.adapters.MovieAdapter;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {
    private static final String ARG_MOVIES = "movies";
    private ArrayList<Movie> movies;
    public static MoviesFragment newInstance(ArrayList<Movie> movies) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIES, movies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movies = (ArrayList<Movie>) getArguments().getSerializable(ARG_MOVIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Tools.setRecyclerView(view.findViewById(R.id.recyclerView_movies), new MovieAdapter(getContext(), movies), new GridLayoutManager(getContext(), 2));
    }
}