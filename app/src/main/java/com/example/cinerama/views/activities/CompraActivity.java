package com.example.cinerama.views.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.models.Silla;
import com.example.cinerama.views.fragments.CompraExitosaFragment;
import com.example.cinerama.views.fragments.ConfirmarCompraFragment;
import com.example.cinerama.views.fragments.PagoFragment;

import java.util.ArrayList;

public class CompraActivity extends AppCompatActivity {

    private ArrayList<Silla> boletos;
    private Proyeccion proyeccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ///recover data
        Intent intent = getIntent();
        this.boletos = (ArrayList<Silla>) intent.getSerializableExtra("asientos"); //String
        this.proyeccion = (Proyeccion) intent.getSerializableExtra("proyeccion");
        //set elements from view
        firstStep();
    }

    private void firstStep(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_compra, PagoFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void secondStep(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_compra, ConfirmarCompraFragment.newInstance(boletos, proyeccion))
                .addToBackStack(null)
                .commit();
    }

    public void thirdStep(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_compra, CompraExitosaFragment.newInstance(true))
                .addToBackStack(null)
                .commit();
    }
}