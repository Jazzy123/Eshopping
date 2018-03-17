package com.eshoppingfyp.eshoppinglatest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;

/**
 * Created by Jahanzeb on 3/2/2018.
 */

public class CustomListView extends ArrayAdapter<String> {

    private String[] ProductNames;
    private String[] Prices;
    private Bitmap[] img;
    private Activity context;

    public CustomListView(Activity context, String[] ProductNames, String[] Prices, Bitmap[] imgId) {
        super(context, R.layout.product_listview,ProductNames);

        this.context = context;
        this.ProductNames = ProductNames;
        this.Prices = Prices;
        this.img = imgId;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.product_listview,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{

            viewHolder = (ViewHolder) r.getTag();

        }
        viewHolder.prodI.setImageBitmap(img[position]);
        viewHolder.prodN.setText(ProductNames[position]);
        viewHolder.prodP.setText(Prices[position]);

        return r;
    }

    class ViewHolder{

        TextView prodN;
        TextView prodP;
        ImageView prodI;
        ViewHolder (View v) {

            prodN = (TextView) v.findViewById(R.id.productName);
            prodP = (TextView) v.findViewById(R.id.productPrice);
            prodI = (ImageView) v.findViewById(R.id.productImage);

        }


    }
}
