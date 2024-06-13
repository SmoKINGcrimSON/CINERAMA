package com.example.cinerama.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinerama.R;
import java.util.ArrayList;

public class CityApapter extends RecyclerView.Adapter<CityApapter.CityViewHolder> {
    Context context;
    ArrayList<String> proyeccions;
    public CityApapter(Context context, ArrayList<String> proyeccions){
        this.context = context;
        this.proyeccions = proyeccions;
    }
    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.city_row, parent, false);
        return new CityApapter.CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityApapter.CityViewHolder holder, int position) {
        holder.city_name.setText(proyeccions.get(position));
        holder.itemView.setOnClickListener(e -> {
            Intent result = new Intent();
            result.putExtra("filtro", proyeccions.get(position));
            ((AppCompatActivity) context).setResult(Activity.RESULT_OK, result);
            ((AppCompatActivity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return proyeccions.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder{
        TextView city_name;
        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.city_name);
        }
    }
}
