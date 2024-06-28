package com.example.cinerama.services;

import com.example.cinerama.endpoints.SalaAPI;
import com.example.cinerama.models.Silla;
import com.example.cinerama.utils.Tools;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SalaService {
    private final SalaAPI apiService;
    public SalaService(String URI){
        Retrofit retrofit = Tools.genApiContext(URI);
        apiService = retrofit.create(SalaAPI.class);
    }
    //methods
    public CompletableFuture<ArrayList<Silla>> getSala(){
        CompletableFuture<ArrayList<Silla>> sala = new CompletableFuture<>();
        Call<ArrayList<Silla>> call = apiService.getSala();
        call.enqueue(new Callback<ArrayList<Silla>>() {
            @Override
            public void onResponse(Call<ArrayList<Silla>> call, Response<ArrayList<Silla>> response) {
                sala.complete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Silla>> call, Throwable throwable) {
                sala.completeExceptionally(throwable);
            }
        });
        return sala;
    }
}
