package com.example.cinerama.endpoints;

import com.example.cinerama.models.Proyeccion;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProyeccionAPI {
    @GET("/cinerama/proyeccion")
    Call<ArrayList<Proyeccion>> getProyecciones();
}
