package com.example.cinerama.models;

import java.io.Serializable;

public class Proyeccion implements Serializable {
    private int id;
    private String hora;
    private String formato;
    private String id_pelicula;
    private String lenguaje;
    private Cinema cinema;

    public Proyeccion(int id, String hora, String formato, String id_pelicula, String lenguaje, Cinema cinema) {
        this.id = id;
        this.hora = hora;
        this.formato = formato;
        this.id_pelicula = id_pelicula;
        this.lenguaje = lenguaje;
        this.cinema = cinema;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(String id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    ///INNER CLASS CINEMA

    public static class Cinema implements Serializable{
        public Cinema(String ciudad, String avenida) {
            this.ciudad = ciudad;
            this.avenida = avenida;
        }
        private String ciudad;
        private String avenida;

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getAvenida() {
            return avenida;
        }

        public void setAvenida(String avenida) {
            this.avenida = avenida;
        }
    }
}
