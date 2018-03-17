package com.eshoppingfyp.eshoppinglatest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText name_,email_,pass_,address_;
    private Button register_;
    private String nameText,emailText,passText,addressText;
    private static final String REGISTER_URL = "http://kasper7860.000webhostapp.com/Eshop/registration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        name_ = (EditText) findViewById(R.id.Rname);
        email_ = (EditText) findViewById(R.id.Remail);
        pass_ = (EditText) findViewById(R.id.Rpassword);
        address_ = (EditText) findViewById(R.id.Raddress);
        register_ = (Button) findViewById(R.id.Rsubmit);



       register_.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(name_.getText().length()==0 && email_.getText().length()==0 && pass_.getText().length()==0
                       && address_.getText().length()==0){

                   name_.setError("Enter Name");
                   email_.setError("Enter Email");
                   pass_.setError("Enter Password");
                   address_.setError("Enter Address");
               }
               else if(name_.getText().length()==0){
                   name_.setError("Enter Name");
               }
               else if(email_.getText().length()==0){
                   email_.setError("Enter Email");
               }
               else if(pass_.getText().length()==0){
                   pass_.setError("Enter Password");
               }
               else if(address_.getText().length()==0){
                   address_.setError("Enter Address");
               }
               else if(!emailValidator(email_.getText().toString())){
                   email_.setError("Invalid email address");
               }
               else {
                   registerUser();
               }
           }
       });

    }

    private void registerUser() {

        nameText = name_.getText().toString().trim();
        emailText = email_.getText().toString().trim();
        passText = pass_.getText().toString().trim();
        addressText = address_.getText().toString().trim();
        register(nameText,emailText,passText,addressText);

    }

    private void register(String nameText, String emailText, String passText, String addressText) {

        String urlsuffix="?name="+nameText+"&email="+emailText+"&password="+passText+"&address="+addressText;
        class RegisterUser extends AsyncTask<String,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this,"Registering","Please Wait",true);

            }

            @Override
            protected void onPostExecute(String a){
                super.onPostExecute(a);
                loading.dismiss();
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);

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
                    Log.d("tttt", "ffffff");
                    return result;

                }catch (Exception e){
                    Log.d("ttttt",e.toString());
                    return null;
                }


            }


        }

        RegisterUser ur = new RegisterUser();
        ur.execute(urlsuffix);


    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
