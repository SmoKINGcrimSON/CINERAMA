package com.example.cinerama.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.example.cinerama.models.Comida;

import java.util.ArrayList;

public class DbComidas extends DbHelper{
    Context context;
    public DbComidas(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    /*
    public long insertarComida(Comida comida){
        long id = 0;
        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", comida.getId());
            values.put("nombre", comida.getNombre());
            values.put("descripcion", comida.getDescripcion());
            values.put("foto", comida.getFoto());
            values.put("precio", comida.getPrecio());
            id = db.insert(TABLE_COMIDAS, null, values);
        }
        catch (Exception ex){
            ex.toString();
        }
        return id;
    } */
    public long insertarComida(Comida comida) {
        long id = 0;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", comida.getId());
        values.put("nombre", comida.getNombre());
        values.put("descripcion", comida.getDescripcion());
        values.put("foto", comida.getFoto());
        values.put("precio", comida.getPrecio());

        try {
            // Comprobar si la comida ya existe en la base de datos
            String selection = "id = ?";
            String[] selectionArgs = { String.valueOf(comida.getId()) };
            Cursor cursor = db.query(TABLE_COMIDAS, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // La comida ya existe, realizar la actualización
                db.update(TABLE_COMIDAS, values, selection, selectionArgs);
                id = comida.getId(); // Devolver el id de la comida actualizada
            } else {
                // La comida no existe, realizar la inserción
                id = db.insert(TABLE_COMIDAS, null, values);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }

        return id;
    }

    public ArrayList<Comida> readComidas(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Comida> comidas = new ArrayList<>();
        Comida comida = null;
        Cursor cursorComidas = null;
        cursorComidas = db.rawQuery("SELECT * FROM " + TABLE_COMIDAS, null);
        if(cursorComidas.moveToFirst()){
            do {
                //FILL COMIDA
                comida = new Comida(
                        cursorComidas.getInt(0),
                        cursorComidas.getString(1),
                        cursorComidas.getString(2),
                        cursorComidas.getString(3),
                        cursorComidas.getFloat(4)
                );
                comidas.add(comida);
            }
            while (cursorComidas.moveToNext());
        }
        cursorComidas.close();
        return comidas;
    }
}
