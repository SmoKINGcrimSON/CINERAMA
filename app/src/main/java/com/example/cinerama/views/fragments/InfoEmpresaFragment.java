package com.example.cinerama.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cinerama.R;

public class InfoEmpresaFragment extends Fragment {

    public static InfoEmpresaFragment newInstance() {
        InfoEmpresaFragment fragment = new InfoEmpresaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_empresa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        TextView text_aboutUs = view.findViewById(R.id.text_aboutUs);
        text_aboutUs.setText("Desde nuestra creación, Cinerama ha trabajado incansablemente para brindar a nuestros clientes las mejores proyecciones de películas en un ambiente cómodo y moderno. Con más de una década de experiencia, hemos crecido y evolucionado para adaptarnos a los avances tecnológicos y las expectativas de nuestros espectadores.");
    }
}