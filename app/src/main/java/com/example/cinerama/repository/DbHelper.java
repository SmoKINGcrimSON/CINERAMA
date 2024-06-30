package com.example.cinerama.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 9; //change when new table or columns are added or discarded
    private static final String DATABASE_NAME = "movies.db";
    public static final String TABLE_MOVIES = "Movie";
    public static final String TABLE_PROYECCIONES = "Proyeccion";
    public static final String TABLE_COMIDAS = "Comida";
    public static final String TABLE_BOLETOS = "Boleto";
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ///TABLE MOVIES
        db.execSQL("CREATE TABLE " + TABLE_MOVIES + "(" +
                "id STRING PRIMARY KEY," +
                "Title STRING," +
                "Year STRING," +
                "Rated STRING," +
                "Released STRING," +
                "Genre STRING," +
                "Director STRING," +
                "Writer STRING," +
                "Actors STRING," +
                "Plot STRING," +
                "Language STRING," +
                "Poster STRING," +
                "Duration INTEGER," +
                "estreno INTEGER)");
        ///TABLE PROYECCION
        db.execSQL("CREATE TABLE " + TABLE_PROYECCIONES + "(" +
                "id INTEGER PRIMARY KEY," +
                "formato STRING," +
                "id_pelicula STRING," +
                "lenguaje STRING," +
                "ciudad STRING," +
                "avenida STRING," +
                "fecha STRING)");
        ///TABLE COMIDA
        db.execSQL("CREATE TABLE " + TABLE_COMIDAS + "(" +
                "id INTEGER PRIMARY KEY," +
                "nombre STRING," +
                "descripcion STRING," +
                "foto STRING," +
                "precio FLOAT)");
        ///TABLE BOLETA
        db.execSQL("CREATE TABLE " + TABLE_BOLETOS + "(" +
                "id STRING PRIMARY KEY," +
                "qr STRING," +
                "pelicula_id INTEGER," +
                "proyeccion_id INTEGER," +
                "user_email STRING," +
                "asiento STRING," +
                "horario STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROYECCIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIDAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOLETOS);
        onCreate(db);
    }
}
