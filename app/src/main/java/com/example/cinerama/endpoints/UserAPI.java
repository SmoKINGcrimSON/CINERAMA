package com.example.cinerama.endpoints;

import com.example.cinerama.models.User;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserAPI {
    @GET("/cinerama/users")
    Call<ArrayList<User>> getUsers();
}
