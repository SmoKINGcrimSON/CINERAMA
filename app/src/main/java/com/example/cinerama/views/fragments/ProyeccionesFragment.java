package com.example.cinerama.views.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.cinerama.R;
import com.example.cinerama.controllers.ProyeccionController;
import com.example.cinerama.repository.DbProyecciones;
import com.example.cinerama.services.ProyeccionService;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.utils.NetworkChangeObserver;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.activities.CalendarActivity;
import com.example.cinerama.views.activities.LocationActivity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ProyeccionesFragment extends Fragment implements Serializable {

    public ArrayList<Proyeccion> proyeccions;
    private String filterCity = null;
    private String filterCalendar = null;
    private ActivityResultLauncher<Intent> launcherCity;
    private ActivityResultLauncher<Intent> launcherCalendar;
    private static final String ARG_ID_MOVIE = "movie_id";
    private String movie_id = "";
    private ProyeccionController controller;

    public static ProyeccionesFragment newInstance(String movie_id) {
        ProyeccionesFragment fragment = new ProyeccionesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID_MOVIE, movie_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) movie_id = (String) getArguments().getSerializable(ARG_ID_MOVIE);
        //city launcher
        launcherCity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            filterCity = data.getStringExtra("filtro");
                            applyFilterAndRender(); //render again
                        }
                    }
                }
        );
        //calendar launcher
        launcherCalendar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                   if(result.getResultCode() == AppCompatActivity.RESULT_OK) {
                       Intent data = result.getData();
                       if(data != null){
                           filterCalendar = data.getStringExtra("filtro");
                           applyFilterAndRender(); //render again
                       }
                   }
                });
        //controller
        controller = new ProyeccionController(new ProyeccionService("https://663b85f9fee6744a6ea1f43e.mockapi.io"),
                new DbProyecciones(getContext()), movie_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proyecciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ////NETWORK LISTENER
        NetworkChangeObserver networkChangeObserver = new NetworkChangeObserver(connected -> {
            if(connected){
                CompletableFuture.supplyAsync(() -> controller.fetchMovies())
                        .thenCompose(p -> p)
                        .thenAccept(p -> {
                            this.proyeccions = p;
                            renderProyeccions();
                        });
            }
            else{
                this.proyeccions = controller.getProyeccionsFromDB();
                renderProyeccions();
            }
        });
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(networkChangeObserver, filter);
        events(view);
    }

    //render
    private void renderProyeccions(){
        if(proyeccions == null) return;
        Tools.genFragment((AppCompatActivity) getContext(), HorariosFragment.newInstance(proyeccions), proyeccions, R.id.frame_layout_proyecciones);
    }

    private void applyFilterAndRender() {
        ArrayList<Proyeccion> filterProyeccions = proyeccions;
        if(filterCity != null){
            filterProyeccions = controller.filterProyeccionByCity(filterProyeccions, filterCity);
        }
        if(filterCalendar != null){
            filterProyeccions = controller.filterProyeccionByDay(filterProyeccions, filterCalendar);
        }
        Tools.genFragment((AppCompatActivity) getContext(), HorariosFragment.newInstance(filterProyeccions), filterProyeccions, R.id.frame_layout_proyecciones);
    }

    private void clearFilterAndRender() {
        Tools.genFragment((AppCompatActivity) getContext(), HorariosFragment.newInstance(proyeccions), proyeccions, R.id.frame_layout_proyecciones);
    }

    //events
    private void events(View view){
        //elements
        ImageButton btn_location = view.findViewById(R.id.btn_location);
        ImageButton btn_clear_filters = view.findViewById(R.id.btn_clear_filters);
        ImageButton btn_date = view.findViewById(R.id.btn_date);
        //events
        btn_clear_filters.setOnClickListener(e -> clearFilterAndRender());
        btn_location.setOnClickListener(e -> {
            Intent intent = Tools.getActivity((AppCompatActivity) getContext(), LocationActivity.class, proyeccions,"proyeccions");
            launcherCity.launch(intent);
        });
        btn_date.setOnClickListener(e -> {
            Intent intent = Tools.getActivity((AppCompatActivity) getContext(), CalendarActivity.class, proyeccions, "proyeccions");
            launcherCalendar.launch(intent);
        });
    }
}