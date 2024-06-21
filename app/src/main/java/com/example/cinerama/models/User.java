package com.example.cinerama.models;

import java.io.Serializable;

public class User implements Serializable {
    private String nickname;
    private String name;
    private String email;
    private String register_date;
    public User(String nickname, String name, String email, String register_date){
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.register_date = register_date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public static class UserAuthenticated{
        private boolean auth;
        private String token;

        public UserAuthenticated(boolean auth, String token){
            this.auth = auth;
            this.token = token;
        }

        public boolean isAuth() {
            return auth;
        }

        public void setAuth(boolean auth) {
            this.auth = auth;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class UserForm{
        private String password;
        private String email;
        public UserForm(String email, String password){
            this.password = password;
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
