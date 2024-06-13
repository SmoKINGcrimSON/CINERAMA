package com.example.cinerama.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cinerama.R;
import com.example.cinerama.models.Comida;

import java.util.ArrayList;

public class ComidaAdapter extends RecyclerView.Adapter<ComidaAdapter.ComidaViewHolder> {
    Context context;
    ArrayList<Comida> comidas;
    public ComidaAdapter(Context context, ArrayList<Comida> comidas){
        this.context = context;
        this.comidas = comidas;
    }
    @NonNull
    @Override
    public ComidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dulce_row, parent, false);
        return new ComidaAdapter.ComidaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidaViewHolder holder, int position) {
        holder.comida_title.setText(comidas.get(position).getNombre());
        holder.comida_title.setText(comidas.get(position).getDescripcion());
        holder.comida_precio.setText(Float.toString(comidas.get(position).getPrecio()) + " S/.");
        Glide.with(holder.itemView.getContext())
                .load(comidas.get(position).getFoto())
                .placeholder(R.drawable.image_broke)
                .error(R.drawable.image_broke)
                .into(holder.comida_foto);
    }

    @Override
    public int getItemCount() {
        return comidas.size();
    }

    public static class ComidaViewHolder extends RecyclerView.ViewHolder{
        TextView comida_title;
        TextView comida_descripcion;
        TextView comida_precio;
        ImageView comida_foto;
        public ComidaViewHolder(@NonNull View itemView) {
            super(itemView);
            comida_title = itemView.findViewById(R.id.comida_title);
            comida_descripcion = itemView.findViewById(R.id.comida_descripcion);
            comida_foto = itemView.findViewById(R.id.comida_foto);
            comida_precio = itemView.findViewById(R.id.comida_precio);
        }
    }
}
