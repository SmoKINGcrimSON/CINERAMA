package com.example.cinerama.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.models.User;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ///recover data
        Intent intent = getIntent();
        users = (ArrayList<User>) intent.getSerializableExtra("users");
        ///evets
        ((Button) findViewById(R.id.btn_login)).setOnClickListener(e -> {
            String email = ((TextView) findViewById(R.id.email)).getText().toString().trim().toLowerCase();
            String password = ((TextView) findViewById(R.id.password)).getText().toString().trim().toLowerCase();
            boolean userExists = users.stream().anyMatch(u -> u.getEmail().toLowerCase().equals(email) && u.getPassword().toLowerCase().equals(password));
            if(userExists) {
                Intent result = new Intent();
                result.putExtra("isConnected", true);
                result.putExtra("username", users.stream().filter(u -> u.getEmail().toLowerCase().equals(email) && u.getPassword().toLowerCase().equals(password))
                        .findFirst().get().getUsername());
                setResult(Activity.RESULT_OK, result);
                this.finish();
            }
            else Toast.makeText(getApplicationContext(), "No pudo logearse", Toast.LENGTH_LONG).show();
        });
    }
}