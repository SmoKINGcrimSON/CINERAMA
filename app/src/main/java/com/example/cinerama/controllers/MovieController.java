package com.example.cinerama.controllers;

import android.content.Context;
import com.example.cinerama.models.Movie;
import com.example.cinerama.repository.DbMovies;
import com.example.cinerama.services.MovieService;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MovieController {

    private MovieService service;
    private DbMovies dbMovies;
    public MovieController(MovieService service, DbMovies dbMovies){
        this.service = service;
        this.dbMovies = dbMovies;
    }

    public CompletableFuture<ArrayList<Movie>> fetchMovies() {
        return CompletableFuture.supplyAsync(() -> service.getMovies())
                .thenCompose(m -> m)
                .thenApply(m -> {
                    PersistenceData(m);
                    return getMoviesFromDB();
                });
    }

    public ArrayList<Movie> getMoviesFromDB(){
        ArrayList<Movie> movies = dbMovies.readMovies();
        return movies;
    }

    private void PersistenceData(ArrayList<Movie> movies){
        movies.forEach(movie -> dbMovies.insertMovie(movie));
    }
}
