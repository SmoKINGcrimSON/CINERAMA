package com.example.cinerama.models;

import java.io.Serializable;

public class Silla implements Serializable {
    private String id;
    private int proyeccion_id;
    private int columna;
    private String fila;
    private boolean disponible;

    public Silla(String id, int proyeccion_id, int columna, String fila, boolean disponible) {
        this.id = id;
        this.columna = columna;
        this.fila = fila;
        this.disponible = disponible;
        this.proyeccion_id = proyeccion_id;
    }

    // Getters y setters...

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProyeccion_id() {
        return proyeccion_id;
    }

    public void setProyeccion_id(int proyeccion_id) {
        this.proyeccion_id = proyeccion_id;
    }
}
