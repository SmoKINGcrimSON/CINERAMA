package com.example.cinerama.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class UserAuthentication {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public UserAuthentication(Context context){
        sp = context.getSharedPreferences("UserAuthentication", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setToken(String token){
        editor.putString("token", token);
        editor.commit();
    }
    public String getToken(){
        return sp.getString("token", "");
    }
    public void delete(){
        editor.clear();
        editor.apply();
    }
}
