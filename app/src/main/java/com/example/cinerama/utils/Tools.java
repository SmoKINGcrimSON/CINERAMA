package com.example.cinerama.utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class Tools {
    public static Retrofit genApiContext(String URI){
        return new Retrofit.Builder().baseUrl(URI).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static Retrofit genApiContextWithAuthentication(String URI, OkHttpClient client){
        return new Retrofit
                .Builder()
                .baseUrl(URI)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static void genActivity(AppCompatActivity context, Class<? extends AppCompatActivity> activity, Serializable data, String dataName){
        if(data == null) return;
        Intent intent = new Intent(context, activity);
        intent.putExtra(dataName, data);
        context.startActivity(intent);
    }
    public static Intent getActivity(AppCompatActivity context, Class<? extends AppCompatActivity> activity, Serializable data, String dataName){
        if(data == null) return null;
        Intent intent = new Intent(context, activity);
        intent.putExtra(dataName, data);
        return intent;
    }
    public static void genActivity(AppCompatActivity context, Class<? extends AppCompatActivity> activity, ArrayList<Serializable> data, ArrayList<String> dataName){
        if(data == null) return;
        Intent intent = new Intent(context, activity);
        //intent.putExtra(dataName, data);
        for(int i = 0; i < data.size(); i++){
            intent.putExtra(dataName.get(i), data.get(i));
        }
        context.startActivity(intent);
    }
    public static void genFragment(AppCompatActivity context, Fragment fragment, Serializable data, int frameLayout){
        if(data == null) return;
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout, fragment) ///-> frame_layout from MainActivity
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    ///Views!
    public static void setRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager){
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }
    public static ViewPager2 setViewPage(ViewPager2 viewPager2, RecyclerView.Adapter adapter){
        viewPager2.setAdapter(adapter);
        return viewPager2;
    }
    public static void moveCarousel(ViewPager2 viewPager2, RecyclerView.Adapter adapter, int miliseconds){
        long AUTO_SCROLL_INTERVAL = miliseconds;
        //CONFIG AUTOSCROLL
        Handler autoScrollHandler = new Handler();
        Runnable autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager2.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= adapter.getItemCount()) nextItem = 0;
                int finalNextItem = nextItem;
                viewPager2.post(() -> viewPager2.setCurrentItem(finalNextItem, true));
                autoScrollHandler.postDelayed(this, AUTO_SCROLL_INTERVAL);
            }
        };
        //INIT AUTOSCROLL
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_IDLE) autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);
                else autoScrollHandler.removeCallbacks(autoScrollRunnable);
            }
        });
    }
    ///fancy looks
    public static void changeButtonColor(ImageButton old, ImageButton current, String oldColor, String newColor){
        int color = Color.parseColor(oldColor);
        ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        Drawable drawable;
        if(old != null) {
            drawable = old.getDrawable();
            old.setColorFilter(colorFilter);
            old.setImageDrawable(drawable);
        }
        color = Color.parseColor(newColor);
        colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        drawable = current.getDrawable();
        current.setColorFilter(colorFilter);
        current.setImageDrawable(drawable);
    }
    public static void changeButtonColor(Button old, Button current){
        if(old != null) old.setTextColor(Color.parseColor("#000000"));
        current.setTextColor(Color.parseColor("#FF0000"));
    }
}
