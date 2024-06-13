package com.example.cinerama.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cinerama.R;
import com.example.cinerama.models.Movie;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.adapters.PremiereAdapter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private static final String ARG_MOVIES = "movies";
    private ArrayList<Movie> movies;
    public static HomeFragment newInstance(ArrayList<Movie> movies) { ///constructor
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIES, movies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) movies = ((ArrayList<Movie>) getArguments().getSerializable(ARG_MOVIES)).stream().filter(Movie::getEstreno).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PremiereAdapter adapter = new PremiereAdapter(getContext(), movies);
        ViewPager2 viewPager2 = Tools.setViewPage(getView().findViewById(R.id.viewPager_movies), adapter);
        Tools.moveCarousel(viewPager2, adapter, 2000);
    }
}