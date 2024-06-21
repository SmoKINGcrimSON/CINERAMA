package com.example.cinerama.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.example.cinerama.models.Movie;
import java.util.ArrayList;
import java.util.Arrays;

public class DbMovies extends DbHelper {
    Context context;
    public DbMovies(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertMovie(Movie movie) {
        long id = 0;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", movie.getId());
        values.put("Title", movie.getTitle());
        values.put("Year", movie.getYear());
        values.put("Rated", movie.getRated());
        values.put("Released", movie.getReleased());
        StringBuilder genres = new StringBuilder();
        movie.getGenre().forEach(m -> genres.append(m.toString()).append(" "));
        values.put("Genre", genres.toString().trim());
        values.put("Director", movie.getDirector());
        values.put("Writer", movie.getWriter());
        values.put("Actors", movie.getActors());
        values.put("Plot", movie.getPlot());
        values.put("Language", movie.getLanguage());
        values.put("Poster", movie.getPoster());
        values.put("Duration", movie.getDuration());
        values.put("estreno", movie.getEstreno() ? 1 : 0);

        try {
            // Comprobar si la película ya existe en la base de datos
            String selection = "id = ?";
            String[] selectionArgs = { String.valueOf(movie.getId()) };
            Cursor cursor = db.query(TABLE_MOVIES, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // La película ya existe, realizar la actualización
                db.update(TABLE_MOVIES, values, selection, selectionArgs);
                id = Integer.parseInt(movie.getId()); // Devolver el id de la película actualizada
            } else {
                // La película no existe, realizar la inserción
                id = db.insert(TABLE_MOVIES, null, values);
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

    public ArrayList<Movie> readMovies(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Movie> movies = new ArrayList<>();
        Movie movie = null;
        Cursor cursorMovies = null;
        cursorMovies = db.rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
        if(cursorMovies.moveToFirst()){
            do{
                //genres
                String genresString = cursorMovies.getString(5);
                ArrayList<String> genres = new ArrayList<>(Arrays.asList(genresString.split(" ")));
                //estreno
                int estrenoNumber = cursorMovies.getInt(cursorMovies.getColumnIndexOrThrow("estreno"));
                boolean estreno = estrenoNumber == 1;
                //fill movie
                movie = new Movie(
                        cursorMovies.getString(1),
                        cursorMovies.getString(2),
                        cursorMovies.getString(3),
                        cursorMovies.getString(4),
                        genres,
                        cursorMovies.getString(6),
                        cursorMovies.getString(7),
                        cursorMovies.getString(8),
                        cursorMovies.getString(9),
                        cursorMovies.getString(10),
                        cursorMovies.getString(11),
                        cursorMovies.getInt(12),
                        cursorMovies.getString(0));
                movie.setEstreno(estreno);
                movies.add(movie);

            } while (cursorMovies.moveToNext());
        }
        cursorMovies.close();
        return movies;
    }
}
