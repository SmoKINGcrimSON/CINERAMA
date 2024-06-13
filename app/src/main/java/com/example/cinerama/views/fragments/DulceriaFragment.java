package com.example.cinerama.views.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cinerama.R;
import com.example.cinerama.database.DbComidas;
import com.example.cinerama.database.DbHelper;
import com.example.cinerama.models.Comida;
import com.example.cinerama.services.ComidaService;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.activities.MainActivity;
import com.example.cinerama.views.adapters.ComidaAdapter;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class DulceriaFragment extends Fragment {

    public ArrayList<Comida> comidas;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dulceria, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ///CALL SERVICE
        if(MainActivity.isConnectedToInternet){
            CompletableFuture.supplyAsync(() -> {
                ComidaService service = new ComidaService("https://664f5090fafad45dfae3489d.mockapi.io");
                return service.getComidas();
            })
                    .thenCompose(c -> c)
                    .thenApply(c -> fillComidaDB(c))
                    .thenAccept(c -> {
                        this.comidas = c;
                        ComidaAdapter adapter = new ComidaAdapter(getContext(), comidas);
                        ViewPager2 viewPager2 = Tools.setViewPage(getView().findViewById(R.id.viewPager_dulceria), adapter);
                        Tools.moveCarousel(viewPager2, adapter, 2000);
                    });
        }
        else{
            chargeDataWithoutNetworkConnection();
            ComidaAdapter adapter = new ComidaAdapter(getContext(), comidas);
            ViewPager2 viewPager2 = Tools.setViewPage(getView().findViewById(R.id.viewPager_dulceria), adapter);
            Tools.moveCarousel(viewPager2, adapter, 2000);
        }
    }
    //DB DULCERIA
    private ArrayList<Comida> fillComidaDB(ArrayList<Comida> comidas){
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DbComidas dbComidas = new DbComidas(getContext());
        comidas.forEach(comida -> dbComidas.insertarComida(comida));
        return comidas;
    }
    private void chargeDataWithoutNetworkConnection(){
        DbComidas dbComidas = new DbComidas(getContext());
        this.comidas = dbComidas.readComidas();
    }
}