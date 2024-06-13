package com.example.cinerama.services;

import androidx.annotation.NonNull;

import com.example.cinerama.endpoints.MoviesAPI;
import com.example.cinerama.models.Movie;
import com.example.cinerama.utils.Tools;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieService {
    private final MoviesAPI apiService;
    public MovieService(String URI){
        Retrofit retrofit = Tools.genApiContext(URI);
        apiService = retrofit.create(MoviesAPI.class);
    }
    public CompletableFuture<ArrayList<Movie>> getMovies(){
        CompletableFuture<ArrayList<Movie>> movies = new CompletableFuture<>();
        Call<ArrayList<Movie>> call = apiService.getMovies();
        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Movie>> call, @NonNull Response<ArrayList<Movie>> response) {
                movies.complete(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Movie>> call, @NonNull Throwable throwable) {
                movies.completeExceptionally(throwable);
            }
        });
        return movies;
    }
}
