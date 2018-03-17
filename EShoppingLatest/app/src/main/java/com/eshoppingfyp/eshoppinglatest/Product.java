package com.eshoppingfyp.eshoppinglatest;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Product extends Fragment {

    private ListView listView;
    String[] ProductNames = null;
    String[] Prices = null;
    Bitmap[] img = null;
    private View view;
    private ArrayList<Stock> list;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private EditText searchP;
    private Spinner category;
    private ArrayList<Stock> searchData;
    private boolean searched;

    public Product() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product, container, false);

        init();

        searchP = (EditText) view.findViewById(R.id.search);
        category = (Spinner) view.findViewById(R.id.categoryP);

        performAction();

        return view;

    }

    private void parseJSON(String json){

        try{

            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("product_data");

            int count = 0;
            String categoryID,categoryN,barcode,name,description,keyword,sale_price,manufacture_id,quantity,date;
            while(count<jsonArray.length()){

                JSONObject jo = jsonArray.getJSONObject(count);
                categoryID = jo.getString("category_id");
                categoryN = jo.getString("category_name");
                barcode = jo.getString("barcode");
                name = jo.getString("name");
                description = jo.getString("description");
                keyword = jo.getString("keyword");
                sale_price = jo.getString("sale_price");
                manufacture_id = jo.getString("manufacture_id");
                quantity = jo.getString("quantity");
                date = jo.getString("date");

                byte[] decodedString = Base64.decode(jo.getString("image"), Base64.DEFAULT);
                Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                list.add(new Stock(categoryID,categoryN,barcode,name,description,keyword,sale_price,manufacture_id,quantity,date,
                        image));

                count++;

            }

        }catch(Exception e){
        }

        updateList(list);

    }

    private void init(){

        listView = view.findViewById(R.id.allProducts);
        list = new ArrayList<Stock>();
        searchData = new ArrayList<Stock>();
        searched = false;

    }

    private void updateList(ArrayList<Stock> list){

        ProductNames = new String[list.size()];
        Prices = new String[list.size()];
        img = new Bitmap[list.size()];

        for(int i = 0; i < list.size(); i++){

            ProductNames[i] = list.get(i).name;
            Prices[i] = "Price: Rs. "+list.get(i).sale_price;
            img[i] = list.get(i).getImage();

        }

        CustomListView customListView = new CustomListView(getActivity(),ProductNames,Prices,img);
        listView.setAdapter(customListView);

    }

    private void openActivity(ArrayList<Stock> list, int i){

        Intent intent = new Intent(getActivity(), ProductDetail.class);
        intent.putExtra("ptD",list.get(i).getProductDetailShownArray());
        intent.putExtra("bitmap", list.get(i).getImage());
        startActivity(intent);

    }

    private void performAction(){

        new BackgroundTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                openActivity(searched ? searchData : list, i);

            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateListWithData(searchP.getText().toString(), category.getSelectedItem().toString());
                Log.d("itemselected",searchP.getText().toString()+", "+category.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String searchedText = charSequence.toString().toLowerCase();
                updateListWithData(searchedText, category.getSelectedItem().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void updateListWithData(String search, String category){

        if(search.length() == 0 && category.equals("All")){
            searched = false;
            updateList(list);
        }else{
            searchData.clear();
            for(Stock stock: list){

                Log.d("items",category+","+search+","+stock.category_name+","+stock.name);
                if(category.equalsIgnoreCase(stock.category_name) || category.equals("All")){
                    if(stock.name.toLowerCase().startsWith(search)){

                        searchData.add(stock);

                    }
                }

            }
            searched = true;
            updateList(searchData);
        }

    }

    class BackgroundTask extends AsyncTask<Void, Void, String>{

        String json = "";

        @Override
        protected void onPreExecute() {
            json = "http://kasper7860.000webhostapp.com/Eshop/json_request.php";
        }

        @Override
        protected void onPostExecute(String aVoid) {

            parseJSON(aVoid);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
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
