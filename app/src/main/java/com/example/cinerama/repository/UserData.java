package com.example.cinerama.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.cinerama.models.User;

public class UserData {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public UserData(Context context){
        sp = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setUser(User u){
        editor.putString("nickname", u.getNickname());
        editor.putString("name", u.getName());
        editor.putString("register_date", u.getRegister_date());
        editor.putString("email", u.getEmail());
        editor.commit();
    }

    public User getUser(){
        return new User(sp.getString("nickname", ""), sp.getString("name", ""), sp.getString("email", ""),
                sp.getString("register_date", ""));
    }

    public void delete(){
        editor.clear();
        editor.apply();
    }
}
