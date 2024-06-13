package com.example.cinerama.singleton;

import com.example.cinerama.models.Proyeccion;
import java.util.ArrayList;
///singleton pattern
public class ProyeccionManager {
    private static ProyeccionManager instance;
    private ArrayList<Proyeccion> proyecciones;

    private ProyeccionManager() {}

    public static synchronized ProyeccionManager getInstance() {
        if (instance == null) {
            instance = new ProyeccionManager();
        }
        return instance;
    }

    public ArrayList<Proyeccion> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(ArrayList<Proyeccion> proyecciones) {
        this.proyecciones = proyecciones;
    }
}