package com.example.cinerama.services;

import com.example.cinerama.endpoints.BoletoAPI;
import com.example.cinerama.models.Boleto;
import com.example.cinerama.utils.Tools;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoletoService {
    private final BoletoAPI apiService;

    public BoletoService(String URI){
        Retrofit retrofit = Tools.genApiContext(URI);
        apiService = retrofit.create(BoletoAPI.class);
    }

    public CompletableFuture<Boleto> crearBoleto(Boleto data){
        CompletableFuture<Boleto> boleto = new CompletableFuture<>();
        Call<Boleto> call = apiService.crearBoleto(data);
        call.enqueue(new Callback<Boleto>() {
            @Override
            public void onResponse(Call<Boleto> call, Response<Boleto> response) {
                boleto.complete(response.body());
            }

            @Override
            public void onFailure(Call<Boleto> call, Throwable throwable) {
                boleto.completeExceptionally(throwable);
            }
        });
        return boleto;
    }

    public CompletableFuture<ArrayList<Boleto>> getBoletos(){
        CompletableFuture<ArrayList<Boleto>> boletos = new CompletableFuture<>();
        Call<ArrayList<Boleto>> call = apiService.getBoletos();
        call.enqueue(new Callback<ArrayList<Boleto>>() {
            @Override
            public void onResponse(Call<ArrayList<Boleto>> call, Response<ArrayList<Boleto>> response) {
                boletos.complete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Boleto>> call, Throwable throwable) {
                boletos.completeExceptionally(throwable);
            }
        });
        return boletos;
    }
}
