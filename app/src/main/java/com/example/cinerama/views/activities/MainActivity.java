package com.example.cinerama.views.activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.controllers.MovieController;
import com.example.cinerama.repository.DbMovies;
import com.example.cinerama.repository.UserAuthentication;
import com.example.cinerama.repository.UserData;
import com.example.cinerama.services.MovieService;
import com.example.cinerama.models.Movie;
import com.example.cinerama.models.User;
import com.example.cinerama.utils.NetworkChangeObserver;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.fragments.DulceriaFragment;
import com.example.cinerama.views.fragments.HomeFragment;
import com.example.cinerama.views.fragments.InfoEmpresaFragment;
import com.example.cinerama.views.fragments.MoviesFragment;
import com.example.cinerama.views.fragments.SearchFragment;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    //FIELDS
    public static boolean isConnectedToInternet;
    private User user;
    private ArrayList<Movie> movies;
    private MovieController controller;
    private ImageButton currentButton;
    //METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initialize variables
        controller = new MovieController(new MovieService("https://663b85f9fee6744a6ea1f43e.mockapi.io"), new DbMovies(this)); //injection controller
        currentButton = null;
        isConnectedToInternet = false;
        //networkChangeObserver
        NetworkChangeObserver networkChangeObserver = new NetworkChangeObserver(connected -> {
            if (connected) {
                this.isConnectedToInternet = true;
                getMoviesWithNetwork();
            }
            else{
                this.isConnectedToInternet = false;
                getMoviesWithoutNetwork();
            }
        });
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeObserver, filter);
    }
    //recover data from controller
    private void getMoviesWithNetwork(){
        CompletableFuture.supplyAsync(() -> controller.fetchMovies())
                .thenCompose(m -> m)
                .thenAccept(m -> {
                    this.movies = m;
                    chargeInitialFragment();
                });
    }
    private void getMoviesWithoutNetwork(){
        this.movies = controller.getMoviesFromDB();
        chargeInitialFragment();
    }
    //events
    private void events(){
        ((ImageButton) findViewById(R.id.btn_home)).setOnClickListener(e -> {
            Tools.genFragment(this, HomeFragment.newInstance(movies), movies, R.id.frame_layout);
            Tools.changeButtonColor(currentButton, findViewById(R.id.btn_home), "#000000", "#FFFFFF");
            currentButton = findViewById(R.id.btn_home);
        });
        ((ImageButton) findViewById(R.id.btn_movies)).setOnClickListener(e -> {
            Tools.genFragment(this, MoviesFragment.newInstance(movies), movies, R.id.frame_layout);
            Tools.changeButtonColor(currentButton, findViewById(R.id.btn_movies), "#000000", "#FFFFFF");
            currentButton = findViewById(R.id.btn_movies);
        });
        ((ImageButton) findViewById(R.id.btn_candies)).setOnClickListener(e -> {
            Tools.genFragment(this, DulceriaFragment.newInstance(), "dulces", R.id.frame_layout);
            Tools.changeButtonColor(currentButton, findViewById(R.id.btn_candies), "#000000", "#FFFFFF");
            currentButton = findViewById(R.id.btn_candies);
        });
        ((ImageButton) findViewById(R.id.btn_more_info)).setOnClickListener(e -> {
            Tools.genFragment(this, InfoEmpresaFragment.newInstance(), "dulces", R.id.frame_layout);
            Tools.changeButtonColor(currentButton, findViewById(R.id.btn_more_info), "#000000", "#FFFFFF");
            currentButton = findViewById(R.id.btn_more_info);
        });
        ((ImageButton) findViewById(R.id.btn_search)).setOnClickListener(e -> {
            Tools.genFragment(this, SearchFragment.newInstance(movies), movies, R.id.frame_layout);
        });
        if(this.user.getName() == ""){
            ((ImageButton) findViewById(R.id.btn_user)).setOnClickListener(e -> {
                Tools.genActivity(this, LoginActivity.class, "", "");
            });
        }
        else {
            ((ImageButton) findViewById(R.id.btn_user)).setImageResource(R.drawable.logout_icon);
            ((ImageButton) findViewById(R.id.btn_user)).setOnClickListener(e -> {
                //eliminar datos en sharedPreferences
                new UserData(this).delete();
                new UserAuthentication(this).delete();
                ((ImageButton) findViewById(R.id.btn_user)).setImageResource(R.drawable.user);
                onStart();
            });
        }
    }
    //render
    @Override
    protected void onStart() {
        super.onStart();
        //set user data
        TextView username = findViewById(R.id.username);
        user = new UserData(this).getUser();
        username.setText(user.getNickname());
        //call events
        events();
    }
    private void chargeInitialFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, HomeFragment.newInstance(this.movies)).addToBackStack(null).commit();
        Tools.changeButtonColor(currentButton, findViewById(R.id.btn_home), "#000000", "#FFFFFF");
        currentButton = findViewById(R.id.btn_home);
    }
}