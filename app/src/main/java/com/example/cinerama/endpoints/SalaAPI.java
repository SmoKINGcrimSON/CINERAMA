package com.example.cinerama.endpoints;

import com.example.cinerama.models.Silla;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SalaAPI {
    @GET("/cinerama/Asiento")
    Call<ArrayList<Silla>> getSala();
    @PUT("/cinerama/Asiento/{id}")
    Call<Silla> updateSilla(@Path("id") String id, @Body Silla silla);
}
