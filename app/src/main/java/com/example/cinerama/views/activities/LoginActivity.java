package com.example.cinerama.views.activities;

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
import com.example.cinerama.repository.UserAuthentication;
import com.example.cinerama.repository.UserData;
import com.example.cinerama.models.User;
import com.example.cinerama.services.UserService;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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
        //variables
        Button btn_signIn = (Button) findViewById(R.id.btn_login);
        //call api
        btn_signIn.setOnClickListener(l -> {
            //
            String email = ((TextView) findViewById(R.id.email)).getText().toString();
            String password = ((TextView) findViewById(R.id.password)).getText().toString();
            //
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            //asynchronous operation
            CompletableFuture.supplyAsync(() -> {
                        UserService userService = new UserService("https://users-cinerama-production.up.railway.app", this);
                        return userService.signIn(new User.UserForm(email, password));
                    })
                    .thenCompose(u -> u)
                    .thenAccept(userAuthenticated -> {
                        //HERE FILL THE TOKEN
                        if(!userAuthenticated.isAuth()) {
                            Toast.makeText(LoginActivity.this, "No pudo iniciar sesión", Toast.LENGTH_LONG).show();
                            return;
                        }
                        UserAuthentication auth = new UserAuthentication(this);
                        auth.setToken(userAuthenticated.getToken());
                    })
                    .thenRun(() -> {
                        CompletableFuture.supplyAsync(() -> {
                            UserService userService = new UserService("https://users-cinerama-production.up.railway.app", this);
                            return userService.getUserInformation();
                        })
                                .thenCompose(u -> u)
                                .thenAccept(u -> {
                                    UserData userData = new UserData(this);
                                    userData.setUser(u);
                                    Toast.makeText(LoginActivity.this, "ACCESS SUCCESSFULLY!", Toast.LENGTH_LONG).show();
                                    this.finish();
                                })
                                .exceptionally(throwable -> {
                                    Toast.makeText(LoginActivity.this, "CAN'T STORED DATA", Toast.LENGTH_LONG).show();
                                    return null;
                                });
                    })
                    .exceptionally(throwable -> {
                        Toast.makeText(LoginActivity.this, "Authentication unsuccessful. isAuth: " + false, Toast.LENGTH_LONG).show();
                        return null;
                    });
            });
    }
}