package com.example.cinerama.services;

import com.example.cinerama.endpoints.ComidaAPI;
import com.example.cinerama.models.Comida;
import com.example.cinerama.utils.Tools;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ComidaService {
    private final ComidaAPI apiService;

    public ComidaService(String URI){
        Retrofit retrofit = Tools.genApiContext(URI);
        apiService = retrofit.create(ComidaAPI.class);
    }
    public CompletableFuture<ArrayList<Comida>> getComidas(){
        CompletableFuture<ArrayList<Comida>> comidas = new CompletableFuture<>();
        Call<ArrayList<Comida>> call = apiService.getComidas();
        call.enqueue(new Callback<ArrayList<Comida>>() {
            @Override
            public void onResponse(Call<ArrayList<Comida>> call, Response<ArrayList<Comida>> response) {
                comidas.complete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Comida>> call, Throwable throwable) {
                comidas.completeExceptionally(throwable);
            }
        });
        return comidas;
    }
}
