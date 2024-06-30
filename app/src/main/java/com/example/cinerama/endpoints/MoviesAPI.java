package com.example.cinerama.endpoints;

import com.example.cinerama.models.Movie;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesAPI {
    @GET("/cinerama/movies")
    Call<ArrayList<Movie>> getMovies();

    @GET("/cinerama/movies/{id}")
    Call<Movie> getMovie(@Path("id") String id);
}
