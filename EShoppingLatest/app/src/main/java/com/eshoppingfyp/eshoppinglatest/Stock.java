package com.eshoppingfyp.eshoppinglatest;

import android.graphics.Bitmap;

/**
 * Created by Jahanzeb on 3/3/2018.
 */

public class Stock {

    public String category_id, category_name, barcode,
        name, description, keyword, sale_price, manuf_price,
        quantity, date;
    public Bitmap image;

    public Stock(String p1, String p2, String p3, String p4,
                 String p5, String p6, String p7, String p8,
                 String p9, String p10, Bitmap bitmap){

        this.category_id = p1;
        this.category_name = p2;
        this.barcode = p3;
        this.name = p4;
        this.description = p5;
        this.keyword = p6;
        this.sale_price = p7;
        this.manuf_price = p8;
        this.quantity = p9;
        this.date = p10;
        this.image = bitmap;

    }

    public String[] getProductDetailShownArray(){

        return new String[]{category_name, name, sale_price, description, quantity, date, barcode};

    }

    public Bitmap getImage(){

        return this.image;

    }

}
