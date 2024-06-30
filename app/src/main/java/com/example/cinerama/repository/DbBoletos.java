package com.example.cinerama.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.example.cinerama.models.Boleto;
import java.util.ArrayList;

public class DbBoletos extends DbHelper{
    Context context;
    public DbBoletos(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    public long insertarBoleto(Boleto boleto){
        long id = 0;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", boleto.getId());
        values.put("qr", boleto.getQr());
        values.put("pelicula_id", boleto.getPelicula_id());
        values.put("proyeccion_id", boleto.getProyeccion_id());
        values.put("user_email", boleto.getUser_email());
        values.put("asiento", boleto.getAsiento());
        values.put("horario", boleto.getHorario());

        try{
            String selection = "id = ?";
            String[] selectionArgs = { String.valueOf(boleto.getId()) };
            Cursor cursor = db.query(TABLE_BOLETOS, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                db.update(TABLE_BOLETOS, values, selection, selectionArgs);
                id = boleto.getId();
            }else{
                id = db.insert(TABLE_BOLETOS, null, values);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        } finally {
            db.close();
        }

        return id;
    }

    public ArrayList<Boleto> readBoletos(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Boleto> boletos = new ArrayList<>();
        Boleto boleto = null;
        Cursor cursorBoletos = null;
        cursorBoletos = db.rawQuery("SELECT * FROM " + TABLE_BOLETOS, null);
        if(cursorBoletos.moveToFirst()){
            do{
                //FILL BOLETOS
                boleto = new Boleto(
                        cursorBoletos.getInt(0),
                        cursorBoletos.getString(1),
                        cursorBoletos.getString(2),
                        cursorBoletos.getInt(3),
                        cursorBoletos.getString(4),
                        cursorBoletos.getString(5),
                        cursorBoletos.getString(6)
                );
                boletos.add(boleto);
            }
            while (cursorBoletos.moveToNext());
        }
        cursorBoletos.close();
        return boletos;
    }
}
