package com.example.cinerama.models;

public class Boleto {
    private Integer id;
    private String qr;
    private String pelicula_id;
    private int proyeccion_id;
    private String user_email;
    private String asiento;

    public Boleto(int id, String qr, String pelicula_id, int proyeccion_id, String user_email, String asiento){
        this.id = id;
        this.qr = qr;
        this.pelicula_id = pelicula_id;
        this.proyeccion_id = proyeccion_id;
        this.user_email = user_email;
        this.asiento = asiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public int getProyeccion_id() {
        return proyeccion_id;
    }

    public void setProyeccion_id(int proyeccion_id) {
        this.proyeccion_id = proyeccion_id;
    }

    public String getPelicula_id() {
        return pelicula_id;
    }

    public void setPelicula_id(String pelicula_id) {
        this.pelicula_id = pelicula_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }
}