package com.example.cinerama.services;

import com.example.cinerama.endpoints.ProyeccionAPI;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.utils.Tools;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProyeccionService {
    private final ProyeccionAPI apiService;
    public ProyeccionService(String URI){
        Retrofit retrofit = Tools.genApiContext(URI);
        apiService = retrofit.create(ProyeccionAPI.class);
    }
    //methods
    public CompletableFuture<ArrayList<Proyeccion>> getProyecciones(){
        CompletableFuture<ArrayList<Proyeccion>> proyeccions = new CompletableFuture<>();
        Call<ArrayList<Proyeccion>> call = apiService.getProyecciones();
        call.enqueue(new Callback<ArrayList<Proyeccion>>() {
            @Override
            public void onResponse(Call<ArrayList<Proyeccion>> call, Response<ArrayList<Proyeccion>> response) {
                proyeccions.complete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Proyeccion>> call, Throwable throwable) {
                proyeccions.completeExceptionally(throwable);
            }
        });
        return proyeccions;
    }
}
