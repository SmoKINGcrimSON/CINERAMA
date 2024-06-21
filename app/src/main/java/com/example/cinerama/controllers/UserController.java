package com.example.cinerama.controllers;

import android.content.Context;
import android.widget.Toast;
import com.example.cinerama.models.User;
import com.example.cinerama.repository.UserAuthentication;
import com.example.cinerama.services.UserService;
import java.util.concurrent.CompletableFuture;

public class UserController {
    private final UserService service;
    private final Context context;
    public UserController(UserService service, Context context){
        this.service = service;
        this.context = context;
    }
    public CompletableFuture<Boolean> signIn(String email, String password){
        return CompletableFuture.supplyAsync(() -> {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Email and password must not be empty", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    return service.signIn(new User.UserForm(email, password));
                })
                .thenCompose(u -> u)
                .thenApply(userAuthenticated -> {
                    boolean isAuthenticated = false;
                    //FILL BOOLEAN
                    if(!userAuthenticated.isAuth()) Toast.makeText(context, "No pudo iniciar sesión", Toast.LENGTH_LONG).show();
                    else{
                        //FILL TOKEN
                        UserAuthentication auth = new UserAuthentication(context);
                        auth.setToken(userAuthenticated.getToken());
                        isAuthenticated = true;
                    }
                    return isAuthenticated;
                })
                .exceptionally(throwable -> {
                    Toast.makeText(context, "Authentication unsuccessful. isAuth: " + false, Toast.LENGTH_LONG).show();
                    return false;
                });
    }
    public CompletableFuture<User> getUser(){
        return CompletableFuture.supplyAsync(service::getUserInformation)
                .thenCompose(u -> u)
                .thenApply(u -> u);
    }
}
