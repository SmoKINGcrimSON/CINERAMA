package com.example.cinerama.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.example.cinerama.models.Proyeccion;
import java.util.ArrayList;

public class DbProyecciones extends DbHelper{
    Context context;
    public DbProyecciones(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    /*
    public long insertProyeccion(Proyeccion proyeccion){
        long id = 0;
        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", proyeccion.getId());
            values.put("hora", proyeccion.getHora());
            values.put("formato", proyeccion.getFormato());
            values.put("id_pelicula", proyeccion.getId_pelicula());
            values.put("lenguaje", proyeccion.getLenguaje());
            values.put("ciudad", proyeccion.getCinema().getCiudad());
            values.put("avenida", proyeccion.getCinema().getAvenida());
            id = db.insert(TABLE_PROYECCIONES, null, values);
        }
        catch (Exception ex){
            ex.toString();
        }
        return id;
    } */
    public long insertProyeccion(Proyeccion proyeccion) {
        long id = 0;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", proyeccion.getId());
        values.put("hora", proyeccion.getHora());
        values.put("formato", proyeccion.getFormato());
        values.put("id_pelicula", proyeccion.getId_pelicula());
        values.put("lenguaje", proyeccion.getLenguaje());
        values.put("ciudad", proyeccion.getCinema().getCiudad());
        values.put("avenida", proyeccion.getCinema().getAvenida());

        try {
            // Comprobar si la proyección ya existe en la base de datos
            String selection = "id = ?";
            String[] selectionArgs = { String.valueOf(proyeccion.getId()) };
            Cursor cursor = db.query(TABLE_PROYECCIONES, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // La proyección ya existe, realizar la actualización
                db.update(TABLE_PROYECCIONES, values, selection, selectionArgs);
                id = proyeccion.getId(); // Devolver el id de la proyección actualizada
            } else {
                // La proyección no existe, realizar la inserción
                id = db.insert(TABLE_PROYECCIONES, null, values);
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
    public ArrayList<Proyeccion> readProyecciones() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Proyeccion> proyecciones = new ArrayList<>();
        Proyeccion proyeccion = null;
        Cursor cursorProyecciones = null;
        cursorProyecciones = db.rawQuery("SELECT * FROM " + TABLE_PROYECCIONES, null);
        if(cursorProyecciones.moveToFirst()){
            do{
                //create Cinema
                String ciudad = cursorProyecciones.getString(5);
                String avenida = cursorProyecciones.getString(6);
                Proyeccion.Cinema cinema = new Proyeccion.Cinema(ciudad, avenida);
                //fill proyeccion
                proyeccion = new Proyeccion(
                        cursorProyecciones.getInt(0),
                        cursorProyecciones.getString(1),
                        cursorProyecciones.getString(2),
                        cursorProyecciones.getString(3),
                        cursorProyecciones.getString(4),
                        cinema
                );
                proyecciones.add(proyeccion);
            }
            while (cursorProyecciones.moveToNext());
        }
        cursorProyecciones.close();
        return proyecciones;
    }
}
