package com.example.cinerama.views.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.cinerama.R;
import com.example.cinerama.models.Boleto;
import com.example.cinerama.models.Movie;
import com.example.cinerama.models.User;
import com.example.cinerama.repository.DbBoletos;
import com.example.cinerama.repository.DbMovies;
import com.example.cinerama.repository.UserData;
import com.example.cinerama.services.BoletoService;
import com.example.cinerama.services.MovieService;
import com.example.cinerama.utils.NetworkChangeObserver;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.activities.MainActivity;
import com.example.cinerama.views.adapters.BoletoAdapter;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompraExitosaFragment extends Fragment {
    private ArrayList<Boleto> boletos;
    private ArrayList<Movie> movies;
    private Button btn_regresar;
    private static final String ARG_BUTTON_HOME_ACTIVATED = "btn_home";
    private boolean btn_home_activated;

    public static CompraExitosaFragment newInstance(Boolean btn_home_activated) {
        CompraExitosaFragment fragment = new CompraExitosaFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_BUTTON_HOME_ACTIVATED, btn_home_activated);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.btn_home_activated = getArguments().getBoolean(ARG_BUTTON_HOME_ACTIVATED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compra_exitosa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //btn settings
        this.btn_regresar = view.findViewById(R.id.btn_regresar_home);
        if(!btn_home_activated) this.btn_regresar.setVisibility(View.INVISIBLE);
        //network observer
        NetworkChangeObserver networkChangeObserver = new NetworkChangeObserver(connected -> {
            if(connected){
                //create futures
                ArrayList<CompletableFuture<?>> futures = new ArrayList<>();
                //add futures
                CompletableFuture.supplyAsync(() -> {
                            BoletoService service = new BoletoService("https://664f5090fafad45dfae3489d.mockapi.io");
                            return service.getBoletos();
                        })
                        .thenCompose(boletos -> boletos)
                        .thenAccept(boletos -> {
                            User user = new UserData(getContext()).getUser();
                            this.boletos = boletos.stream().filter(b -> b.getUser_email().equalsIgnoreCase(user.getEmail())).collect(Collectors.toCollection(ArrayList::new));
                        })
                        .thenRun(() -> {
                            CompletableFuture.supplyAsync(() -> {
                                        MovieService service = new MovieService("https://663b85f9fee6744a6ea1f43e.mockapi.io");
                                        return service.getMovies();
                                    })
                                    .thenCompose(movies -> movies)
                                    .thenAccept(movies -> {
                                        //persistence data
                                        this.persistenceData(boletos);
                                        //recover movies present in boletos
                                        this.movies = movies.stream().filter(
                                                        m -> this.boletos.stream().anyMatch(b -> b.getPelicula_id().equalsIgnoreCase(m.getId())))
                                                .collect(Collectors.toCollection(ArrayList::new));
                                        //set up viewPager2
                                        Tools.setViewPage(view.findViewById(R.id.viewPager2_boletos), new BoletoAdapter(getContext(), this.boletos, this.movies));
                                        this.btn_regresar.setEnabled(true);
                                        //
                                        this.btn_regresar.setOnClickListener(e -> {
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            getActivity().finish();
                                        });
                                    });
                        });
            }
            else{
                this.boletos = getBoletosFromDB();
                DbMovies dbMovies = new DbMovies(getContext());
                this.movies = dbMovies.readMovies();
                Tools.setViewPage(view.findViewById(R.id.viewPager2_boletos), new BoletoAdapter(getContext(), this.boletos, this.movies));
            }
        });
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkChangeObserver, filter);
    }
    //DB
    public ArrayList<Boleto> getBoletosFromDB(){
        DbBoletos dbBoletos = new DbBoletos(getContext());
        ArrayList<Boleto> boletos = dbBoletos.readBoletos();
        return boletos;
    }

    private void persistenceData(ArrayList<Boleto> boletos){
        DbBoletos dbBoletos = new DbBoletos(getContext());
        boletos.forEach(boleto -> dbBoletos.insertarBoleto(boleto));
    }
}