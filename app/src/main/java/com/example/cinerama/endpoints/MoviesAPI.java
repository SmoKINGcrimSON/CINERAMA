package com.example.cinerama.endpoints;

import com.example.cinerama.models.Movie;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MoviesAPI {
    @GET("/cinerama/movies")
    Call<ArrayList<Movie>> getMovies();
}
