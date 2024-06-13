package com.example.cinerama.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cinerama.R;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.adapters.ProyeccionAdapter;
import java.util.ArrayList;

public class HorariosFragment extends Fragment {
    private static final String ARG_PROYECCIONES = "proyecciones";
    private ArrayList<Proyeccion> proyeccions;
    public static HorariosFragment newInstance(ArrayList<Proyeccion> proyeccions) {
        HorariosFragment fragment = new HorariosFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROYECCIONES, proyeccions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) proyeccions = (ArrayList<Proyeccion>) getArguments().getSerializable(ARG_PROYECCIONES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_horarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Tools.setRecyclerView(view.findViewById(R.id.recyclerView_Proyecciones), new ProyeccionAdapter(view.getContext(), proyeccions), new LinearLayoutManager(getContext()));
    }
}