package com.example.cinerama.views.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.models.Silla;
import com.example.cinerama.views.fragments.SalaFragment;
import java.util.ArrayList;

public class SalaActivity extends AppCompatActivity {

    private static final int ROWS = 10;
    private static final int COLUMNS = 8;
    private ArrayList<Silla> sala;
    private int proyeccion_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sala);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //data Intent
        Intent intent = getIntent();
        proyeccion_id = intent.getIntExtra("proyeccion_id", -1);
        //charge fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.sala_id, SalaFragment.newInstance(this.proyeccion_id)).addToBackStack(null).commit();
    }
}