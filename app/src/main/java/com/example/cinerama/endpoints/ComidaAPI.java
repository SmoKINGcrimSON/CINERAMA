package com.example.cinerama.endpoints;

import com.example.cinerama.models.Comida;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ComidaAPI {
    @GET("/cinerama/Comida")
    Call<ArrayList<Comida>> getComidas();
}
