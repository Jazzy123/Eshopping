package com.eshoppingfyp.eshoppinglatest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {

    private TextView name, price, quantity, descrip, date, category;
    private ImageView imageP;
    private Button fav,cart;
    String barcode;
    private static final String REGISTER_URL = "http://kasper7860.000webhostapp.com/Eshop/AddFavorites.php";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        name = (TextView) findViewById(R.id.productname);
        price = (TextView) findViewById(R.id.price);
        quantity = (TextView) findViewById(R.id.quantity);
        descrip = (TextView) findViewById(R.id.description);
        date = (TextView) findViewById(R.id.expirydate);
        category = (TextView) findViewById(R.id.categoryName);
        imageP = (ImageView) findViewById(R.id.imageView3);
        listView = (ListView) findViewById(R.id.reviewsList);

        String[] productDetails = getIntent().getStringArrayExtra("ptD");
        Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("bitmap");

        category.setText(productDetails[0]);
        name.setText(productDetails[1]);
        price.setText(productDetails[2]);
        descrip.setText(productDetails[3]);
        quantity.setText(productDetails[4]);
        date.setText(productDetails[5]);
        barcode = productDetails[6];
        imageP.setImageBitmap(bitmap);


        fav = (Button) findViewById(R.id.favB);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFav();
            }
        });

        showReviews();

    }


    private void AddFav(){

        SharedPreferences sharedPreferences = getSharedPreferences("tech", MODE_PRIVATE);
        String uname = sharedPreferences.getString("email", "");
        String barcode_ = barcode;
        AddToFav(uname,barcode_);

    }

    private void parseJSON(String jsonData){

        ArrayList<String> reviews = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("product_data");

            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject object = jsonArray.getJSONObject(i);
                reviews.add(object.getString("review"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.review_list, R.id.review_textview, reviews);
        listView.setAdapter(arrayAdapter);

    }

    private void showReviews(){

        class Revi extends AsyncTask<String,Void,String>{

            ProgressDialog loading;
            String link = "http://kasper7860.000webhostapp.com/Eshop/getreviews.php?barcode="+barcode;


            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ProductDetail.this,"Loading Reviews","Please Wait",true);

            }

            @Override
            protected void onPostExecute(String a){
                super.onPostExecute(a);
                parseJSON(a);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {

                BufferedReader bufferReader = null;

                try {
                    URL url = new URL(link);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferReader.readLine();

                    return result;

                }catch (Exception e){
                    return null;
                }

            }
        }

        Revi revi = new Revi();
        revi.execute();

    }

    private void AddToFav(String uname, String barcode_) {

        String urlsuffix="?email="+uname+"&barcode="+barcode_;
        class ToFav extends AsyncTask<String,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ProductDetail.this,"Adding to Favorite","Please Wait",true);

            }

            @Override
            protected void onPostExecute(String a){
                super.onPostExecute(a);
                loading.dismiss();

            }

            @Override
            protected String doInBackground(String... params) {

                String a = params[0];
                BufferedReader bufferReader = null;

                try {
                    URL url = new URL(REGISTER_URL+a);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferReader.readLine();

                    return result;

                }catch (Exception e){
                   return null;
                }

              }
        }

        ToFav toFav = new ToFav();
        toFav.execute(urlsuffix);

    }

    @Override
    public void onBackPressed() {

        finish();

    }
}
