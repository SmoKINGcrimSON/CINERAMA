package com.example.cinerama.views.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.models.Silla;
import com.example.cinerama.repository.UserData;
import com.example.cinerama.services.MovieService;
import com.example.cinerama.utils.NetworkChangeObserver;
import com.example.cinerama.views.fragments.SalaFragment;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class SalaActivity extends AppCompatActivity {
    public ArrayList<Silla> asientos = new ArrayList<>(); //String
    public Button btn_comprar;
    private Proyeccion proyeccion;

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
        //data recover from Intent
        Intent intent = getIntent();
        proyeccion = (Proyeccion) intent.getSerializableExtra("proyeccion");
        //charge fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.sala_id, SalaFragment.newInstance(this.proyeccion.getId())).addToBackStack(null).commit();
        //btn comprar
        this.btn_comprar = findViewById(R.id.btn_comprar);
        btn_comprar.setEnabled(false); //desactivar
        //lanzar actividad comprar
        btn_comprar.setOnClickListener(l -> {
            NetworkChangeObserver networkChangeObserver = new NetworkChangeObserver(connected -> {
                if (connected && !new UserData(this).getUser().getEmail().isBlank()){
                    Intent newIntent = new Intent(this, CompraActivity.class);
                    newIntent.putExtra("asientos", asientos);
                    newIntent.putExtra("proyeccion", proyeccion);
                    startActivity(newIntent);
                }
                else{
                    this.finish();
                }
            });
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            this.registerReceiver(networkChangeObserver, filter);
        });
    }
}