package com.example.cinerama.views.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.controllers.UserController;
import com.example.cinerama.repository.UserData;
import com.example.cinerama.services.UserService;
import java.util.concurrent.CompletableFuture;

public class LoginActivity extends AppCompatActivity {

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
        Button btn_signIn = findViewById(R.id.btn_login);
        UserController controller = new UserController(new UserService("https://users-cinerama-production.up.railway.app", this), this);
        //methods
        this.authenticateUserEvent(btn_signIn, controller);
    }

    //events
    public void authenticateUserEvent(Button btn_signIn, UserController controller){
        btn_signIn.setOnClickListener(l -> {
            String email = ((EditText) findViewById(R.id.email)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            CompletableFuture.supplyAsync(() -> controller.signIn(email, password))
                    .thenCompose(auth -> auth)
                    .thenApply(auth -> auth)
                    .thenAccept(auth -> {
                        if(!auth) return;
                        CompletableFuture.supplyAsync(controller::getUser)
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
                        Toast.makeText(this, "Authentication unsuccessful. isAuth: " + false, Toast.LENGTH_LONG).show();
                        return null;
                    });
        });
    }
}