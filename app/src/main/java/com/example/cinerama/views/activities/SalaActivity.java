package com.example.cinerama.views.activities;

import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cinerama.R;

public class SalaActivity extends AppCompatActivity {

    private static final int ROWS = 10;
    private static final int COLUMNS = 8;
    private static final int MARGIN = 16; // Tamaño del margen en píxeles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sala);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Crear el ConstraintLayout
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ));

        // Crear TextView para la pantalla
        TextView screenTextView = new TextView(this);
        screenTextView.setId(View.generateViewId());
        screenTextView.setText("Pantalla");
        screenTextView.setTextSize(24);
        screenTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        screenTextView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        constraintLayout.addView(screenTextView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Conectar el TextView al top del layout
        constraintSet.connect(screenTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(screenTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(screenTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

        // Crear Guidelines horizontales y verticales
        Guideline[] horizontalGuidelines = createHorizontalGuidelines(constraintLayout);
        Guideline[] verticalGuidelines = createVerticalGuidelines(constraintLayout);

        // Crear botones de asientos
        createSeats(constraintLayout, constraintSet, horizontalGuidelines, verticalGuidelines);

        constraintSet.applyTo(constraintLayout);
        setContentView(constraintLayout);
    }

    private Guideline[] createHorizontalGuidelines(ConstraintLayout layout) {
        Guideline[] guidelines = new Guideline[ROWS + 1]; // +1 para incluir la guía inferior
        for (int i = 0; i <= ROWS; i++) {
            guidelines[i] = new Guideline(this);
            guidelines[i].setId(View.generateViewId());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.orientation = ConstraintLayout.LayoutParams.HORIZONTAL;
            params.guidePercent = (i + 1) / (float) (ROWS + 2); // Ajustar para incluir espacio superior e inferior
            guidelines[i].setLayoutParams(params);
            layout.addView(guidelines[i]);
        }
        return guidelines;
    }

    private Guideline[] createVerticalGuidelines(ConstraintLayout layout) {
        Guideline[] guidelines = new Guideline[COLUMNS + 1]; // +1 para incluir la guía derecha
        for (int i = 0; i <= COLUMNS; i++) {
            guidelines[i] = new Guideline(this);
            guidelines[i].setId(View.generateViewId());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.orientation = ConstraintLayout.LayoutParams.VERTICAL;
            params.guidePercent = (i + 1) / (float) (COLUMNS + 2); // Ajustar para incluir espacio izquierdo y derecho
            guidelines[i].setLayoutParams(params);
            layout.addView(guidelines[i]);
        }
        return guidelines;
    }

    private void createSeats(ConstraintLayout layout, ConstraintSet constraintSet, Guideline[] horizontalGuidelines, Guideline[] verticalGuidelines) {
        char[] rows = "ABCDEFGHIJ".toCharArray();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Button button = new Button(this);
                button.setId(View.generateViewId());
                button.setText(String.format("%c%d", rows[i], j + 1));
                button.setBackgroundColor(Color.parseColor("#FD6C5E")); // Establecer el color de fondo a rojo
                button.setTextColor(Color.WHITE);
                layout.addView(button);

                // Conectar el botón a las guías horizontales y verticales
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                button.setLayoutParams(params);

                constraintSet.constrainWidth(button.getId(), ConstraintSet.WRAP_CONTENT);
                constraintSet.constrainHeight(button.getId(), ConstraintSet.WRAP_CONTENT);

                constraintSet.connect(button.getId(), ConstraintSet.TOP, horizontalGuidelines[i].getId(), ConstraintSet.BOTTOM, MARGIN / 2);
                constraintSet.connect(button.getId(), ConstraintSet.BOTTOM, horizontalGuidelines[i + 1].getId(), ConstraintSet.TOP, MARGIN / 2);
                constraintSet.connect(button.getId(), ConstraintSet.START, verticalGuidelines[j].getId(), ConstraintSet.END, MARGIN / 2);
                constraintSet.connect(button.getId(), ConstraintSet.END, verticalGuidelines[j + 1].getId(), ConstraintSet.START, MARGIN / 2);
            }
        }
    }
}
