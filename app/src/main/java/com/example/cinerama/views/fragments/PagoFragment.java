package com.example.cinerama.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cinerama.R;
import com.example.cinerama.views.activities.CompraActivity;


public class PagoFragment extends Fragment {

    public static PagoFragment newInstance() {
        PagoFragment fragment = new PagoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pago, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recover elements
        EditText card_number_txt = view.findViewById(R.id.card_number_txt);
        EditText card_date_txt = view.findViewById(R.id.card_date_txt);
        EditText cvc_text = view.findViewById(R.id.cvc_text);
        EditText titular_txt = view.findViewById(R.id.titular_txt);
        Button btn_confirmar = view.findViewById(R.id.btn_confirmar);
        //eventos
        btn_confirmar.setOnClickListener(e -> {
            if(card_number_txt.getText().toString().isBlank()){ //TODO: no olvidar hacer una pequeña validación
                Toast.makeText(getContext().getApplicationContext(), "Ingresa los números", Toast.LENGTH_SHORT).show();
                return;
            }
            if(card_date_txt.getText().toString().isBlank()){
                Toast.makeText(getContext().getApplicationContext(), "Ingresa fecha válida", Toast.LENGTH_SHORT).show();
                return;
            }
            if(cvc_text.getText().toString().isBlank()){
                Toast.makeText(getContext().getApplicationContext(), "Ingresa cvc válido", Toast.LENGTH_SHORT).show();
                return;
            }
            if(titular_txt.getText().toString().isBlank()){
                Toast.makeText(getContext().getApplicationContext(), "Ingresa nombre válido válido", Toast.LENGTH_SHORT).show();
                return;
            }
            CompraActivity compraActivity = (CompraActivity) getActivity();
            compraActivity.secondStep();
        });
    }
}