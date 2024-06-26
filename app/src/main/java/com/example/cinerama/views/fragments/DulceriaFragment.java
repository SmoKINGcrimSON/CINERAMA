package com.example.cinerama.views.fragments;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cinerama.R;
import com.example.cinerama.controllers.DulceriaController;
import com.example.cinerama.repository.DbComidas;
import com.example.cinerama.models.Comida;
import com.example.cinerama.services.ComidaService;
import com.example.cinerama.utils.NetworkChangeObserver;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.adapters.ComidaAdapter;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class DulceriaFragment extends Fragment {

    private ArrayList<Comida> comidas;
    private DulceriaController controller;

    public static DulceriaFragment newInstance() {
        DulceriaFragment fragment = new DulceriaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
        controller = new DulceriaController(new ComidaService("https://664f5090fafad45dfae3489d.mockapi.io"), new DbComidas(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dulceria, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        NetworkChangeObserver networkChangeObserver = new NetworkChangeObserver(connected -> {
            if(connected) getComidaWithNetwork();
            else getComidaWithoutNetwork();
        });
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(networkChangeObserver, filter);
    }

    private void getComidaWithNetwork(){
        CompletableFuture.supplyAsync(() -> controller.fetchComidas())
                .thenCompose(c -> c)
                .thenAccept(c -> {
                    this.comidas = c;
                    this.chargeInitialContent();
                });
    }
    private void getComidaWithoutNetwork(){
        this.comidas = this.controller.getComidaFromDB();
        try{
            chargeInitialContent(); //when connectivity is changing could cause troubles!
        }
        catch (Exception ex){
            Log.d("EXCEPTION", ex.getMessage());
        }
    }
    private void chargeInitialContent(){
        ComidaAdapter adapter = new ComidaAdapter(getContext(), comidas);
        ViewPager2 viewPager2 = Tools.setViewPage(getView().findViewById(R.id.viewPager_dulceria), adapter);
        Tools.moveCarousel(viewPager2, adapter, 2000);
    }
}