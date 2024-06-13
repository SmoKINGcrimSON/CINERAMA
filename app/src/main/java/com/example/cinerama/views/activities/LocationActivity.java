package com.example.cinerama.views.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.cinerama.R;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.adapters.CityApapter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class LocationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        ArrayList<Proyeccion> proyeccions = (ArrayList<Proyeccion>) intent.getSerializableExtra("proyeccions");
        ArrayList<String> passed_proyeccions = proyeccions.stream().map(m -> m.getCinema().getCiudad()).distinct().collect(Collectors.toCollection(ArrayList::new));
        Tools.setRecyclerView(findViewById(R.id.recyclerView_Cities), new CityApapter(this, passed_proyeccions), new LinearLayoutManager(this));
    }
}