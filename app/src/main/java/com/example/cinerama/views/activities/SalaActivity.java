package com.example.cinerama.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.QuickContactBadge;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.models.Silla;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.fragments.SalaFragment;
import java.util.ArrayList;

public class SalaActivity extends AppCompatActivity {
    public ArrayList<String> asientos = new ArrayList<>();
    private static final int ROWS = 10;
    private static final int COLUMNS = 8;
    private ArrayList<Silla> sala;
    private int proyeccion_id;
    public Button btn_comprar;

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
        //launcher
        ActivityResultLauncher<Intent> lancher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            asientos.add(data.getStringExtra("asiento"));
                            Log.d("ASIENTO", asientos.get(asientos.size() - 1));
                        }
                    }
                }
        );
        //data Intent
        Intent intent = getIntent();
        proyeccion_id = intent.getIntExtra("proyeccion_id", -1);
        //charge fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.sala_id, SalaFragment.newInstance(this.proyeccion_id)).addToBackStack(null).commit();
        //btn comprar
        this.btn_comprar = findViewById(R.id.btn_comprar);
        btn_comprar.setEnabled(false); //desactivar
        //lanzar actividad comprar
        btn_comprar.setOnClickListener(l -> {
            ///pasar asientos a nueva actividad
            Tools.genActivity(this, CompraActivity.class, asientos, "asientos");
        });
    }
}