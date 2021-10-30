package com.example.imdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieList extends AppCompatActivity {
LinearLayout listView;
String imageBaseURL = "https://image.tmdb.org/t/p/w500";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        listView = findViewById(R.id.movieListView);
        populateList();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("MovieListData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void populateList() {
        ArrayList<MovieItem> movieItemArrayList  = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = obj.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Gson gson = new Gson();
                MovieItem movieItem=gson.fromJson(jsonObject.toString(),MovieItem.class);
                movieItemArrayList.add(movieItem);
                addMovieToView(movieItem);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addMovieToView(MovieItem movieItem) {
        View view = LayoutInflater.from(MovieList.this.getApplicationContext()).inflate(R.layout.movielist_info, listView, false);
        listView.addView(view);
        TextView title = view.findViewById(R.id.movieTitle);
        title.setText(movieItem.title);
        TextView info = view.findViewById(R.id.movieInfo);
        info.setText(movieItem.overview);
        ImageView poster = view.findViewById(R.id.movieImage);
        String imageURL = imageBaseURL + movieItem.poster_path;
        new ImageLoadTask(imageURL, poster).execute();
    }
}