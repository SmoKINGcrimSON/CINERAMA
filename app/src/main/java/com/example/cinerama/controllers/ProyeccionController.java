package com.example.cinerama.controllers;

import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.repository.DbProyecciones;
import com.example.cinerama.services.ProyeccionService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProyeccionController {

    private ProyeccionService service;
    private DbProyecciones dbProyecciones;
    private String movie_id;

    public ProyeccionController(ProyeccionService service, DbProyecciones dbProyecciones, String movie_id){
        this.service = service;
        this.dbProyecciones = dbProyecciones;
        this.movie_id = movie_id;
    }

    public CompletableFuture<ArrayList<Proyeccion>> fetchMovies(){
        return CompletableFuture.supplyAsync(() -> service.getProyecciones())
                .thenCompose(p -> p)
                .thenApply(p -> {
                    p = filterProyeccionByMovie(p);
                    assert p != null;
                    p = filterProyeccionByDay(p);
                    assert p != null;
                    PersistenceData(p);
                    return getProyeccionsFromDB();
                });
    }

    public ArrayList<Proyeccion> getProyeccionsFromDB(){
        ArrayList<Proyeccion> proyeccions = dbProyecciones.readProyecciones();
        proyeccions = filterProyeccionByMovie(proyeccions);
        return proyeccions;
    }

    private void PersistenceData(ArrayList<Proyeccion> proyeccions){
        proyeccions.forEach(proyeccion -> dbProyecciones.insertProyeccion(proyeccion));
    }

    public ArrayList<Proyeccion> filterProyeccionByCity(ArrayList<Proyeccion> proyeccions, String filter) {
        if (filter == null || filter.isEmpty()) return proyeccions;
        return proyeccions.stream().filter(p -> p.getCinema().getCiudad().equals(filter)).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Proyeccion> filterProyeccionByMovie(ArrayList<Proyeccion> proyeccions) {
        if (movie_id == null || movie_id.isEmpty()) return null;
        return proyeccions.stream().filter(p -> p.getId_pelicula().equals(movie_id)).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Proyeccion> filterProyeccionByDay(ArrayList<Proyeccion> proyeccions){
        return proyeccions.stream().filter(p -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime dateProyeccion = LocalDateTime.parse(p.getFecha(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime datePresent = LocalDateTime.now();
                return datePresent.isBefore(dateProyeccion) && datePresent.toLocalDate().isEqual(dateProyeccion.toLocalDate()); //filter for this day and future proyeccions
            }
            else return false;
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
