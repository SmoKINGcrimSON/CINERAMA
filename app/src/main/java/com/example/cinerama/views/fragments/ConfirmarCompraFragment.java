package com.example.cinerama.views.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.cinerama.R;
import com.example.cinerama.models.Boleto;
import com.example.cinerama.models.Movie;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.models.Silla;
import com.example.cinerama.models.User;
import com.example.cinerama.repository.UserData;
import com.example.cinerama.services.BoletoService;
import com.example.cinerama.services.MovieService;
import com.example.cinerama.services.SalaService;
import com.example.cinerama.views.activities.CompraActivity;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ConfirmarCompraFragment extends Fragment {
    private static final String ARG_BOLETOS = "boletos";
    private static final String ARG_PROYECCION = "proyeccion";
    private ArrayList<Silla> asientos;
    private Proyeccion proyeccion;
    private Movie movie;
    //elementos
    Button btn_confirmar_compra;
    TextView movie_title_txt;
    TextView asientos_txt;
    TextView horario_txt;
    TextView sala_txt;
    TextView monto_txt;

    public static ConfirmarCompraFragment newInstance(ArrayList<Silla> boletos, Proyeccion proyeccion) {
        ConfirmarCompraFragment fragment = new ConfirmarCompraFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOLETOS, boletos);
        args.putSerializable(ARG_PROYECCION, proyeccion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.asientos = (ArrayList<Silla>) getArguments().getSerializable(ARG_BOLETOS);
            this.proyeccion = (Proyeccion) getArguments().getSerializable(ARG_PROYECCION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirmar_compra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("BOLETOS", asientos.get(0).getFila().concat(Integer.toString(asientos.get(0).getColumna())));
        Log.d("PROYECCION", proyeccion.getLenguaje());
        //set elements
        createElements(view);
        //api call
        CompletableFuture.supplyAsync(() -> {
            MovieService service = new MovieService("https://663b85f9fee6744a6ea1f43e.mockapi.io");
            return service.getMovie(proyeccion.getId_pelicula());
        })
                .thenCompose(m -> m)
                .thenAccept(m -> {
                    this.movie = m;
                    Log.d("MOVIE NAME", this.movie.getTitle());
                    render();
                });
    }

    private void render(){
        setElements();
    }

    private void createElements(View view){
        this.btn_confirmar_compra = view.findViewById(R.id.btn_confirmar_compra);
        this.movie_title_txt = view.findViewById(R.id.movie_title_txt);
        this.asientos_txt = view.findViewById(R.id.asientos_txt);
        this.horario_txt = view.findViewById(R.id.horario_txt);
        this.sala_txt = view.findViewById(R.id.sala_txt);
        this.monto_txt = view.findViewById(R.id.monto_txt);
    }

    private void setElements(){
        //button
        btn_confirmar_compra.setEnabled(true);
        btn_confirmar_compra.setOnClickListener(l -> {
            btn_confirmar_compra.setEnabled(false);
            crearBoleto();
        });
        //other elements
        this.movie_title_txt.setText(movie.getTitle());
        StringBuilder asientos_txt = new StringBuilder();
        asientos.forEach(a -> {
            asientos_txt.append(a.getFila()).append(a.getColumna()).append(", ");
        });
        if (asientos_txt.length() > 0) {
            asientos_txt.replace(asientos_txt.length() - 2, asientos_txt.length(), ".");
        }
        this.asientos_txt.setText(asientos_txt);
        this.horario_txt.setText(proyeccion.getFecha());
        this.sala_txt.setText("1");
        this.monto_txt.setText(9.50f * asientos.size() + " (S/).");
    }

    ///generar boleto (POST) y actualizar sillas (PUT)
    private void crearBoleto(){
        CompletableFuture.runAsync(() -> {
            //create services
            BoletoService boletoService = new BoletoService("https://664f5090fafad45dfae3489d.mockapi.io");
            SalaService salaService = new SalaService("https://664f5090fafad45dfae3489d.mockapi.io");
            //recover user from shared preferences
            User user = new UserData(getContext()).getUser();
            //list for save futures
            ArrayList<CompletableFuture<?>> futures = new ArrayList<>(); //Boleto

            //create and save futures!
            asientos.forEach(a -> {
                String asiento = a.getFila() + a.getColumna();

                CompletableFuture<Boleto> futureBoleto = CompletableFuture.supplyAsync(() -> {
                    try {
                        Boleto boleto = new Boleto(1, UUID.randomUUID().toString(), movie.getId(), proyeccion.getId(), user.getEmail(), asiento);
                        return boletoService.crearBoleto(boleto).join();
                    } catch (Exception e) {
                        Log.e("Error", "Error creando boleto para asiento: " + asiento, e);
                        throw new RuntimeException(e);
                    }
                });

                futures.add(futureBoleto);

                CompletableFuture<Silla> futureSilla = CompletableFuture.supplyAsync(() -> {
                    try {
                        a.setDisponible(false);
                        return salaService.updateSilla(a.getId(), a).join();
                    } catch (Exception e) {
                        Log.e("Error", "Error actualizando  el asiento: " + asiento, e);
                        throw new RuntimeException(e);
                    }
                });

                futures.add(futureSilla);
                ///
            });

            // wait for all futures be completed
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            //manage end of all futures
            allFutures.thenRun(() -> {
                boolean allSuccessful = futures.stream().allMatch(CompletableFuture::isDone) &&
                        futures.stream().noneMatch(CompletableFuture::isCompletedExceptionally);
                if (allSuccessful) {
                    Log.d("Terminado", "Todos los boletos creados exitosamente.");
                    CompraActivity activity = (CompraActivity) getActivity();
                    activity.thirdStep(); //ejecutar lo último luego de una compra exitosa
                } else {
                    Log.e("Error", "Ocurrió un error en la creación de los boletos.");
                }
            }).exceptionally(e -> {
                Log.e("Error", "Ocurrió una excepción general: " + e.getMessage());
                return null;
            });
        });
    }
}