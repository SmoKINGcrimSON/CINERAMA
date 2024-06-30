package com.example.cinerama.views.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinerama.R;
import com.example.cinerama.controllers.ProyeccionController;
import com.example.cinerama.models.Proyeccion;
import com.example.cinerama.repository.UserData;
import com.example.cinerama.utils.NetworkChangeObserver;
import com.example.cinerama.utils.Tools;
import com.example.cinerama.views.activities.MainActivity;
import com.example.cinerama.views.activities.SalaActivity;
import java.util.ArrayList;

public class ProyeccionAdapter extends RecyclerView.Adapter<ProyeccionAdapter.ProyeccionViewHolder> {
    ArrayList<Proyeccion> proyeccions;
    Context context;
    public ProyeccionAdapter(Context context, ArrayList<Proyeccion> proyeccions){
        this.proyeccions = proyeccions;
        this.context = context;
    }
    @NonNull
    @Override
    public ProyeccionAdapter.ProyeccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.proyeccion_row, parent, false);
        return new ProyeccionAdapter.ProyeccionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProyeccionAdapter.ProyeccionViewHolder holder, int position) {
        try{
            holder.proyeccion_hora.setText(proyeccions.get(position).getFecha().substring(11, 16));
        }
        catch (Exception ex){
            Log.d("Can't charge the hour of date", ex.toString());
        }
        ProyeccionController controller = new ProyeccionController();
        String fecha = controller.whenMovieWillProject(proyeccions.get(position));
        //
        holder.lenguaje.setText(proyeccions.get(position).getLenguaje());
        holder.formato.setText(proyeccions.get(position).getFormato());
        holder.cine.setText(proyeccions.get(position).getCinema().getCiudad() + ", " + proyeccions.get(position).getCinema().getAvenida());
        holder.fecha.setText(fecha);
        holder.itemView.setOnClickListener(c -> {
            Intent intent = new Intent(context, SalaActivity.class);
            intent.putExtra("proyeccion", proyeccions.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return proyeccions.size();
    }
    public static class ProyeccionViewHolder extends RecyclerView.ViewHolder{
        TextView proyeccion_hora;
        TextView lenguaje;
        TextView formato;
        TextView cine;
        TextView fecha;
        public ProyeccionViewHolder(@NonNull View itemView) {
            super(itemView);
            proyeccion_hora = itemView.findViewById(R.id.proyeccion_hora);
            lenguaje = itemView.findViewById(R.id.lenguaje);
            formato = itemView.findViewById(R.id.formato);
            cine = itemView.findViewById(R.id.cine);
            fecha = itemView.findViewById(R.id.fecha);
        }
    }
}
