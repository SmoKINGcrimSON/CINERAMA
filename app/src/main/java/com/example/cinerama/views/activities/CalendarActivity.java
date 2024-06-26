package com.example.cinerama.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerama.R;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent result = getIntent();
        TextView txt_hoy = findViewById(R.id.txt_hoy);
        TextView txt_manana = findViewById(R.id.txt_manana);
        //
        txt_hoy.setOnClickListener(e -> {
            result.putExtra("filtro", "Hoy");
            this.setResult(Activity.RESULT_OK, result);
            this.finish();
        });
        txt_manana.setOnClickListener(e -> {
            result.putExtra("filtro", "Mañana");
            this.setResult(Activity.RESULT_OK, result);
            this.finish();
        });

    }
}