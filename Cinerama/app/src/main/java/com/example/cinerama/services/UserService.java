package com.example.cinerama.services;

import com.example.cinerama.endpoints.UserAPI;
import com.example.cinerama.models.User;
import com.example.cinerama.utils.Tools;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserService {
    private final UserAPI apiService;
    public UserService(String URI){
        Retrofit retrofit = Tools.genApiContext(URI);
        apiService = retrofit.create(UserAPI.class);
    }
    //methods
    public CompletableFuture<ArrayList<User>> getUsers(){
        CompletableFuture<ArrayList<User>> users = new CompletableFuture<>();
        Call<ArrayList<User>> call = apiService.getUsers();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                users.complete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable throwable) {
                users.completeExceptionally(throwable);
            }
        });
        return users;
    }
}
