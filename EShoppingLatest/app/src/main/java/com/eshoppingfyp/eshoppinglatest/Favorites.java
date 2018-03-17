package com.eshoppingfyp.eshoppinglatest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class Favorites extends Fragment {

    private String sql = "SELECT * FROM Favorites natural join Stock where Favorites.barcode = Stock.barcode and Favorites.email = "+Current.email;
    private View view;
    private ListView listView;
    ArrayList<String> ProductNames = null;
    ArrayList<String> Prices = null;
    ArrayList<String> Barcode = null;
    ArrayList<Bitmap> img = null;
    private ArrayList<Stock> list;
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    public Favorites() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorites, container, false);

        init();
        new BackgroundTask().execute();

        return view;

    }

    private void init(){

        listView = view.findViewById(R.id.favorites_frag);
        list = new ArrayList<Stock>();

    }

    private void parseJSON(String json){

        try{

            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("product_data");

            ProductNames = new ArrayList<>();
            Prices = new ArrayList<>();
            img = new ArrayList<>();
            Barcode = new ArrayList<>();

            int count = 0;
            while(count<jsonArray.length()){

                JSONObject jo = jsonArray.getJSONObject(count);

                byte[] decodedString = Base64.decode(jo.getString("image"), Base64.DEFAULT);
                Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Barcode.add(jo.getString("barcode"));
                ProductNames.add(jo.getString("name"));
                Prices.add(jo.getString("price"));
                img.add(image);

                count++;

            }

        }catch(Exception e){
        }

        FavouriteCustomListView customListView = new FavouriteCustomListView(getActivity(),ProductNames.toArray(new String[]{})
                ,Prices.toArray(new String[]{}),img.toArray(new Bitmap[]{}), Barcode.toArray(new String[]{}));
        listView.setAdapter(customListView);

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String json = "";

        @Override
        protected void onPreExecute() {
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("tech", Context.MODE_PRIVATE);
            String Email = sharedPreferences.getString("email",null);
            json = "http://kasper7860.000webhostapp.com/Eshop/getFavs.php?email="+Email;
        }

        @Override
        protected void onPostExecute(String aVoid) {

            parseJSON(aVoid);

        }

        @Override
        protected String doInBackground(Void... voids) {

            String data = "";

            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(json).openConnection();
                InputStream stream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        stream
                ));

                String line = "";
                while((line = reader.readLine()) != null){

                    data += "\n"+line;

                }

                reader.close();
                stream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
            }

            return data.trim();

        }

    }

}
