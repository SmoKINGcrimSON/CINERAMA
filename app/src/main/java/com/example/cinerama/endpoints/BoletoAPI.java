package com.example.cinerama.endpoints;

import com.example.cinerama.models.Boleto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BoletoAPI {
    @POST("/cinerama/Boleto")
    Call<Boleto> crearBoleto(@Body Boleto boleto);

    @GET("/cinerama/Boleto")
    Call<ArrayList<Boleto>> getBoletos();
}
