package com.eshoppingfyp.eshoppinglatest;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jahanzeb on 3/2/2018.
 */

public class FavouriteCustomListView extends ArrayAdapter<String> {

    private String[] ProductNames;
    private String[] Prices;
    private Bitmap[] img;
    private Activity context;
    private String[] barcode;
    private String email;

    public FavouriteCustomListView(Activity context, String[] ProductNames, String[] Prices, Bitmap[] imgId,
                                   String[] barcode) {

        super(context, R.layout.favorite_list,ProductNames);

        this.context = context;
        this.ProductNames = ProductNames;
        this.Prices = Prices;
        this.img = imgId;
        this.barcode = barcode;
        this.email = context.getSharedPreferences("tech", Context.MODE_PRIVATE).getString("email","");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.favorite_list,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);

        }
        else{

            viewHolder = (ViewHolder) r.getTag();

        }
        viewHolder.prodI.setImageBitmap(img[position]);
        viewHolder.prodN.setText(ProductNames[position]);
        viewHolder.prodP.setText(Prices[position]);
        viewHolder.barcode = this.barcode[position];

        return r;

    }

    class ViewHolder{

        TextView prodN;
        TextView prodP;
        ImageView prodI;
        Button removeBtn;
        Button cartBtn;
        String barcode;

        ViewHolder (View v) {

            prodN = (TextView) v.findViewById(R.id.productName_fav);
            prodP = (TextView) v.findViewById(R.id.productPrice_fav);
            prodI = (ImageView) v.findViewById(R.id.productImage_fav);
            removeBtn = (Button) v.findViewById(R.id.remove_fav);
            cartBtn = (Button) v.findViewById(R.id.add_cart_fav);

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeFav(barcode);

                }
            });
        }

    }

    private void removeFav(final String barcode){

        Log.d("testingprogram", barcode+", "+email);
        final String Burl = "http://kasper7860.000webhostapp.com/Eshop/deleteFavs.php?email="+email+"&barcode="+barcode;

        class RemoveFav extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String a){
                super.onPostExecute(a);


            }

            @Override
            protected String doInBackground(String... params) {

                BufferedReader bufferReader = null;

                try {
                    URL url = new URL(Burl);
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

        new RemoveFav().execute();

    }

}
