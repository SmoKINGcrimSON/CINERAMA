package com.example.cinerama.endpoints;

import com.example.cinerama.models.User;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAPI {
    @POST("/users/signin")
    Call<User.UserAuthenticated> signIn(@Body User.UserForm userform);
    @GET("/users/readById")
    Call<User> getUserInformation();
}
