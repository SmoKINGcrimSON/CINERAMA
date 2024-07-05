package com.example.cinerama.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinerama.R;
import com.example.cinerama.models.Boleto;
import com.example.cinerama.models.Movie;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BoletoAdapter extends RecyclerView.Adapter<BoletoAdapter.BoletoViewHolher> {
    private Context context;
    private ArrayList<Boleto> boletos;
    private ArrayList<Movie> movies;

    public BoletoAdapter(Context context, ArrayList<Boleto> boletos, ArrayList<Movie> movies){
        this.context = context;
        this.boletos = boletos;
        this.movies = movies;
    }

    @NonNull
    @Override
    public BoletoViewHolher onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.boleto_row, parent, false);
        return new BoletoAdapter.BoletoViewHolher(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoletoViewHolher holder, int position) {
        String uuid = boletos.get(position).getQr();
        Bitmap bitmap = null;
        try{
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(uuid, BarcodeFormat.QR_CODE, 400, 400);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(bitmap != null){
            holder.qr_image.setImageBitmap(bitmap);
        }
        else{
            holder.qr_image.setImageResource(R.drawable.image_broke);
        }
        String movie_id = boletos.get(position).getPelicula_id();
        Movie movie = movies.stream().filter(
                        m -> boletos.stream().anyMatch(b -> movie_id.equalsIgnoreCase(m.getId())))
                .findFirst().orElse(null);
        holder.movie_title.setText(movie.getTitle());
        holder.asiento_sala.setText(boletos.get(position).getAsiento());
        holder.movie_sala.setText("1");
        //
        String fechaHoraStr  = boletos.get(position).getHorario();
        int dia = 0;
        String mes = "";
        String hora = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr);

            dia = fechaHora.getDayOfMonth();
            mes = fechaHora.getMonth().toString();
            hora = fechaHora.getHour() + ":" + fechaHora.getMinute();
        }
        //
        holder.horario_txt.setText(dia + " de " + mes + " a las " + hora);
    }

    @Override
    public int getItemCount() {
        return boletos.size();
    }

    public static class BoletoViewHolher extends RecyclerView.ViewHolder {
        ImageView qr_image;
        TextView movie_title;
        TextView asiento_sala;
        TextView movie_sala;
        TextView horario_txt;

        public BoletoViewHolher(@NonNull View itemView) {
            super(itemView);
            qr_image = itemView.findViewById(R.id.qr_image);
            movie_title = itemView.findViewById(R.id.movie_title);
            asiento_sala = itemView.findViewById(R.id.asiento_sala);
            movie_sala = itemView.findViewById(R.id.movie_sala);
            horario_txt = itemView.findViewById(R.id.horario_txt);
        }
    }
}
