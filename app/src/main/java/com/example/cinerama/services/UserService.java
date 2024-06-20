package com.example.cinerama.services;

import android.content.Context;

import com.example.cinerama.endpoints.UserAPI;
import com.example.cinerama.interceptors.UserInterceptor;
import com.example.cinerama.models.User;
import com.example.cinerama.utils.Tools;
import java.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserService {
    Context context;
    private OkHttpClient client;
    private final UserAPI apiService;

    public UserService(String URI, Context context){
        this.context = context;
        client = new OkHttpClient.Builder().addInterceptor(new UserInterceptor(this.context)).build();
        Retrofit retrofit = Tools.genApiContextWithAuthentication(URI, client);
        apiService = retrofit.create(UserAPI.class);
    }
    //methods
    public CompletableFuture<User.UserAuthenticated> signIn(User.UserForm form){
        CompletableFuture<User.UserAuthenticated> userAuthenticated = new CompletableFuture<>();
        Call<User.UserAuthenticated> call = apiService.signIn(form);
        call.enqueue(new Callback<User.UserAuthenticated>() {
            @Override
            public void onResponse(Call<User.UserAuthenticated> call, Response<User.UserAuthenticated> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userAuthenticated.complete(response.body());
                } else {
                    userAuthenticated.completeExceptionally(new Exception("Unsuccessful response: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<User.UserAuthenticated> call, Throwable throwable) {
                userAuthenticated.completeExceptionally(throwable);
            }
        });
        return userAuthenticated;
    }

    public CompletableFuture<User> getUserInformation(){
        CompletableFuture<User> user = new CompletableFuture<>();
        Call<User> call = apiService.getUserInformation();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user.complete(response.body());
                } else {
                    user.completeExceptionally(new Exception("Unsuccessful response: " + response.code() + "don't provide token"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                user.completeExceptionally(throwable);
            }
        });
        return user;
    }
}
