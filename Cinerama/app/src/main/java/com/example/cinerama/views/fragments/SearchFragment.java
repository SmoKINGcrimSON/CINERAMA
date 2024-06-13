package com.example.cinerama.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.example.cinerama.R;
import com.example.cinerama.models.Movie;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.adapters.RowMovieAdapter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchFragment extends Fragment {
    private static final String ARG_MOVIES = "movies";
    private ArrayList<Movie> movies;
    public static SearchFragment newInstance(ArrayList<Movie> movies) {
        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ///
        EditText searchText = view.findViewById(R.id.movie_search);
        searchText.setEnabled(true);
        searchText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        if (imm != null) imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);
        ///
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toLowerCase();
                ArrayList<Movie> search_movies = movies.stream().
                        filter(m -> m.getTitle().toLowerCase().startsWith(search) && !search.trim().isEmpty())
                        .collect(Collectors.toCollection(ArrayList::new));
                Tools.setRecyclerView((RecyclerView) view.findViewById(R.id.recyclerView_movies), new RowMovieAdapter(getContext(), search_movies),
                        new LinearLayoutManager(getContext()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}