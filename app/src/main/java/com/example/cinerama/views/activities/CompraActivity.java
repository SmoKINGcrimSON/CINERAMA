package com.example.cinerama.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import java.util.ArrayList;

public class CompraActivity extends AppCompatActivity {

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
        ///
        Intent intent = getIntent();
        ArrayList<String> boletos = (ArrayList<String>) intent.getSerializableExtra("asientos");
        TextView boletos_txt = findViewById(R.id.boletos_text);
        boletos_txt.setText("asientos escogidos: ");
        boletos.stream().forEach(b -> boletos_txt.append(b + ", "));
    }
}