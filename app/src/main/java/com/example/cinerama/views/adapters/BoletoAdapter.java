package com.example.cinerama.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinerama.R;
import com.example.cinerama.models.Boleto;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class BoletoAdapter extends RecyclerView.Adapter<BoletoAdapter.BoletoViewHolher> {
    private Context context;
    private ArrayList<Boleto> boletos;

    public BoletoAdapter(Context context, ArrayList<Boleto> boletos){
        this.context = context;
        this.boletos = boletos;
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
    }

    @Override
    public int getItemCount() {
        return boletos.size();
    }

    public static class BoletoViewHolher extends RecyclerView.ViewHolder {
        ImageView qr_image;
        public BoletoViewHolher(@NonNull View itemView) {
            super(itemView);
            qr_image = itemView.findViewById(R.id.qr_image);
        }
    }
}
