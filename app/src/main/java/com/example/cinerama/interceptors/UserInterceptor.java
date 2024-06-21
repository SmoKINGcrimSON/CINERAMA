package com.example.cinerama.interceptors;

import android.content.Context;
import androidx.annotation.NonNull;
import com.example.cinerama.repository.UserAuthentication;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserInterceptor implements Interceptor {
    UserAuthentication userAuthentication;

    public UserInterceptor(Context context){
        userAuthentication = new UserAuthentication(context);
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Platform", "Android")
                .addHeader("x-access-token", userAuthentication.getToken())
                .build();
        return chain.proceed(request);
    }

}
