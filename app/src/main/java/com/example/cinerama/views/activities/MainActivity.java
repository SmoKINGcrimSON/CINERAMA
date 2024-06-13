package com.example.cinerama.views.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;
import com.example.cinerama.database.DbHelper;
import com.example.cinerama.database.DbMovies;
import com.example.cinerama.services.MovieService;
import com.example.cinerama.models.Movie;
import com.example.cinerama.models.User;
import com.example.cinerama.services.UserService;
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
    public static boolean isConnectedToInternet = false;
    private ArrayList<Movie> movies;
    private ArrayList<User> users;
    public ImageButton currentButton = null;
    private boolean isConnected = false;
    private String username;
    private final ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            r -> {
                if (r.getResultCode() == RESULT_OK) {
                    Intent data = r.getData();
                    if (data != null) {
                        isConnected = data.getBooleanExtra("isConnected", false);
                        String username = data.getStringExtra("username");
                        if (isConnected) {
                            this.username = username;
                            updateInterface();
                        }
                    }
                }
            }
    );
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
        //charge data
        NetworkChangeObserver networkChangeObserver = new NetworkChangeObserver(connected -> {
            if (connected) {
                this.isConnectedToInternet = true;
                callAPIs();
            }
            else{
                this.isConnectedToInternet = false;
                chargeDataWithoutNetworkConnection();
            }
        });
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeObserver, filter);
        events();
    }
    private void callAPIs(){
        //movies
        CompletableFuture.supplyAsync(() -> {
            MovieService movieService = new MovieService("https://663b85f9fee6744a6ea1f43e.mockapi.io");
            return movieService.getMovies();
        })
                .thenCompose(m -> m)
                .thenAccept(m -> {
                    this.movies = m;
                    fillMovieDB();
                })
                .thenAccept(m -> initialFragment());
        //users
        CompletableFuture.supplyAsync(() -> {
            UserService userService = new UserService("https://664f5090fafad45dfae3489d.mockapi.io");
            return userService.getUsers();
        }).thenCompose(u -> u).thenAccept(u -> this.users = u);
    }
    private void updateInterface() {
        ((ImageButton) findViewById(R.id.btn_user)).setImageResource(R.drawable.logout_icon);
        ((TextView) findViewById(R.id.username)).setText(username);
    }
    private void initialFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, HomeFragment.newInstance(this.movies)).addToBackStack(null).commit();
        Tools.changeButtonColor(currentButton, findViewById(R.id.btn_home), "#000000", "#FFFFFF");
        currentButton = findViewById(R.id.btn_home);
    }
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
        ((ImageButton) findViewById(R.id.btn_user)).setOnClickListener(e -> {
            if(!isConnected){
                Intent intent = Tools.getActivity(this, LoginActivity.class, users, "users");
                if(!isConnectedToInternet) return;
                activityLauncher.launch(intent);
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //Intent.FLAG_ACTIVITY_NEW_TASK |
                startActivity(intent);
            }
        });
    }
    ///DB MOVIES
    private void fillMovieDB(){
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DbMovies dbMovies = new DbMovies(this);
        this.movies.forEach(movie -> dbMovies.insertMovie(movie));
    }
    private void chargeDataWithoutNetworkConnection(){
        DbMovies dbMovies = new DbMovies(this);
        this.movies = dbMovies.readMovies();
        initialFragment();
    }
}