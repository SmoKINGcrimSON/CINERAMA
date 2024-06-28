package com.example.cinerama.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.example.cinerama.R;
import com.example.cinerama.models.Silla;
import com.example.cinerama.services.SalaService;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SalaFragment extends Fragment {

    private static final String ARG_PROYECCION_ID = "proyeccion_id";

    private static final int ROWS = 10;
    private static final int COLUMNS = 8;
    private ArrayList<Silla> sala;
    private int proyeccion_id;
    private ViewGroup rootView;

    public SalaFragment() {

    }

    public static SalaFragment newInstance(Integer proyeccion_id) {
        SalaFragment fragment = new SalaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROYECCION_ID, proyeccion_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.proyeccion_id = (Integer) getArguments().getSerializable(ARG_PROYECCION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sala, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        callAPI();
    }

    private void callAPI(){
        CompletableFuture.supplyAsync(() -> {
                    SalaService service = new SalaService("https://664f5090fafad45dfae3489d.mockapi.io");
                    return service.getSala();
                })
                .thenCompose(sala -> sala)
                .thenAccept(sala -> {
                    this.sala = sala.stream().filter(silla -> silla.getProyeccion_id() == proyeccion_id).collect(Collectors.toCollection(ArrayList::new));
                    getActivity().runOnUiThread(this::render);
                });
    }

    private void render(){
        ConstraintLayout constraintLayout = rootView.findViewById(R.id.constraintLayout);

        // Create ImageView for the Screen
        ImageView screenImageView = new ImageView(getContext());
        screenImageView.setId(View.generateViewId());
        screenImageView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        screenImageView.setImageResource(R.drawable.screen_cinema);

        // Add ImageView to the constraint layout
        constraintLayout.addView(screenImageView);

        // Create a Guideline to separate the screen from the seats
        Guideline screenGuideline = new Guideline(getContext());
        screenGuideline.setId(View.generateViewId());
        ConstraintLayout.LayoutParams screenGuidelineParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        screenGuidelineParams.orientation = ConstraintLayout.LayoutParams.HORIZONTAL;
        screenGuidelineParams.guidePercent = 0.2f;
        screenGuideline.setLayoutParams(screenGuidelineParams);
        constraintLayout.addView(screenGuideline);

        // Create ConstraintSet
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Position ImageView at the top and connect its bottom to the Guideline
        constraintSet.connect(screenImageView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(screenImageView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(screenImageView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(screenImageView.getId(), ConstraintSet.BOTTOM, screenGuideline.getId(), ConstraintSet.TOP);

        // Create Guidelines horizontal and vertical
        Guideline[] horizontalGuidelines = createHorizontalGuidelines(constraintLayout);
        Guideline[] verticalGuidelines = createVerticalGuidelines(constraintLayout);

        // Create buttons for chairs
        createSeats(constraintLayout, constraintSet, horizontalGuidelines, verticalGuidelines, screenGuideline);

        constraintSet.applyTo(constraintLayout);
    }

    private Guideline[] createHorizontalGuidelines(ConstraintLayout layout) {
        Guideline[] guidelines = new Guideline[ROWS + 1];
        for (int i = 0; i <= ROWS; i++) {
            guidelines[i] = new Guideline(getContext());
            guidelines[i].setId(View.generateViewId());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.orientation = ConstraintLayout.LayoutParams.HORIZONTAL;
            params.guidePercent = (i + 1) / (float) (ROWS + 2);
            guidelines[i].setLayoutParams(params);
            layout.addView(guidelines[i]);
        }
        return guidelines;
    }

    private Guideline[] createVerticalGuidelines(ConstraintLayout layout) {
        Guideline[] guidelines = new Guideline[COLUMNS + 1];
        for (int i = 0; i <= COLUMNS; i++) {
            guidelines[i] = new Guideline(getContext());
            guidelines[i].setId(View.generateViewId());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.orientation = ConstraintLayout.LayoutParams.VERTICAL;
            params.guidePercent = (i + 1) / (float) (COLUMNS + 2);
            guidelines[i].setLayoutParams(params);
            layout.addView(guidelines[i]);
        }
        return guidelines;
    }

    private void createSeats(ConstraintLayout layout, ConstraintSet constraintSet, Guideline[] horizontalGuidelines, Guideline[] verticalGuidelines, Guideline screenGuideline) {
        char[] rows = "ABCDEFGHIJ".toCharArray();
        int buttonWidth = getResources().getDimensionPixelSize(R.dimen.button_width);
        int buttonHeight = getResources().getDimensionPixelSize(R.dimen.button_height);
        int margin = getResources().getDimensionPixelSize(R.dimen.button_margin);

        int positionSala = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                String asiento = String.format("%c%d", rows[i], j + 1);

                Button button = new Button(getContext());
                button.setId(View.generateViewId());
                button.setText(asiento);
                button.setTextColor(Color.WHITE);
                button.setPadding(margin, margin, margin, margin);

                if (positionSala < sala.size()) {
                    Silla sillaActual = sala.get(positionSala);
                    String asientoSala = sillaActual.getFila() + sillaActual.getColumna();

                    if (asientoSala.trim().equalsIgnoreCase(asiento.trim())) {
                        if (sala.get(positionSala).isDisponible()) {
                            button.setBackgroundColor(Color.parseColor("#5E6AFD"));
                        } else {
                            button.setBackgroundColor(Color.parseColor("#FD6C5E"));
                        }
                        positionSala++;
                    } else {
                        button.setTextColor(Color.parseColor("#FD6C5E"));
                        layout.addView(button);
                        button.setVisibility(View.INVISIBLE);
                        continue;
                    }
                } else {
                    return;
                }

                layout.addView(button);

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(buttonWidth, buttonHeight);
                params.setMargins(margin, margin, margin, margin);
                button.setLayoutParams(params);

                constraintSet.constrainWidth(button.getId(), buttonWidth);
                constraintSet.constrainHeight(button.getId(), buttonHeight);

                constraintSet.connect(button.getId(), ConstraintSet.TOP,
                        i == 0 ? screenGuideline.getId() : horizontalGuidelines[i].getId(),
                        i == 0 ? ConstraintSet.BOTTOM : ConstraintSet.TOP,
                        margin);
                constraintSet.connect(button.getId(), ConstraintSet.BOTTOM,
                        horizontalGuidelines[i + 1].getId(),
                        ConstraintSet.TOP,
                        margin);
                constraintSet.connect(button.getId(), ConstraintSet.START,
                        verticalGuidelines[j].getId(),
                        ConstraintSet.END,
                        margin);
                constraintSet.connect(button.getId(), ConstraintSet.END,
                        verticalGuidelines[j + 1].getId(),
                        ConstraintSet.START,
                        margin);
            }
        }
    }
}
