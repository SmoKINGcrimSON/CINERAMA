package com.example.cinerama.controllers;

import com.example.cinerama.models.Comida;
import com.example.cinerama.repository.DbComidas;
import com.example.cinerama.services.ComidaService;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class DulceriaController {
    private DbComidas dbComidas;
    private ComidaService service;
    public DulceriaController(ComidaService service, DbComidas dbComidas){
        this.service = service;
        this.dbComidas = dbComidas;
    }
    public CompletableFuture<ArrayList<Comida>> fetchComidas(){
        return CompletableFuture.supplyAsync(() -> service.getComidas())
                .thenCompose(c -> c)
                .thenApply(c -> {
                    PersistenceData(c);
                    return getComidaFromDB();
                });
    }

    public ArrayList<Comida> getComidaFromDB(){
        ArrayList<Comida> comidas = dbComidas.readComidas();
        return comidas;
    }

    private void PersistenceData(ArrayList<Comida> comidas){
        comidas.forEach(comida -> dbComidas.insertarComida(comida));
    }
}
