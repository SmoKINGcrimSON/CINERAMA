package com.example.cinerama.views.fragments;

import android.content.Intent;
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
import com.example.cinerama.singleton.ProyeccionManager;
import com.example.cinerama.services.ProyeccionService;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.activities.LocationActivity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProyeccionesFragment extends Fragment implements Serializable {
    public ArrayList<Proyeccion> proyeccions;
    private String filter;
    private ActivityResultLauncher<Intent> launcher;
    private static final String ARG_ID_MOVIE = "movie_id";
    private String movie_id;
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
        //ActivityResultLauncher
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            filter = data.getStringExtra("filtro");
                            applyFilterAndRender(); //render again
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proyecciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ////CALL SERVICE
        CompletableFuture.supplyAsync(() -> {
            ProyeccionService service = new ProyeccionService("https://663b85f9fee6744a6ea1f43e.mockapi.io");
            return service.getProyecciones();
        })
                .thenCompose(p -> p)
                .thenAccept(p -> {
                    ProyeccionManager.getInstance().setProyecciones(p);
                    filterByMovie(p);
                    setElements(view);
        });
    }
    private void filterByMovie(ArrayList<Proyeccion> proyeccions){
        this.proyeccions = proyeccions.stream().filter(p -> p.getId_pelicula().equals(movie_id)).collect(Collectors.toCollection(ArrayList::new));
    }
    ///ELEMENTS
    private void setElements(View view){
        if(proyeccions == null) return;
        Tools.genFragment((AppCompatActivity) getContext(), HorariosFragment.newInstance(proyeccions), proyeccions, R.id.frame_layout_proyecciones);
        ///button
        ImageButton btn_location = view.findViewById(R.id.btn_location);
        ImageButton btn_clear_filters = view.findViewById(R.id.btn_clear_filters);
        //events
        btn_location.setOnClickListener(e -> {
            Intent intent = Tools.getActivity((AppCompatActivity) getContext(), LocationActivity.class, proyeccions,"proyeccions");
            launcher.launch(intent);
        });
        btn_clear_filters.setOnClickListener(e -> clearFilterAndRender());
    }
    //FILTERS
    private void applyFilterAndRender() {
        if (filter != null && !filter.isEmpty()) {
            ArrayList<Proyeccion> filteredProyeccions = proyeccions
                    .stream()
                    .filter(p -> p.getCinema().getCiudad().equals(filter))
                    .collect(Collectors.toCollection(ArrayList::new));
            Tools.genFragment((AppCompatActivity) getContext(), HorariosFragment.newInstance(filteredProyeccions), filteredProyeccions, R.id.frame_layout_proyecciones);
        }
    }
    private void clearFilterAndRender() {
        this.proyeccions = proyeccions.stream().filter(p -> p.getId_pelicula().equals(movie_id)).collect(Collectors.toCollection(ArrayList::new));
        Tools.genFragment((AppCompatActivity) getContext(), HorariosFragment.newInstance(proyeccions), proyeccions, R.id.frame_layout_proyecciones);
    }
}