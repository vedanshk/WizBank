package com.example.wizbank.Authencation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wizbank.Database.DatabaseHelper;
import com.example.wizbank.MainActivity;
import com.example.wizbank.Models.User;
import com.example.wizbank.R;
import com.example.wizbank.Utils;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText edtTxtEmail , edtTxtPassword , edtTtPassword , edtTxtAddress , edtTxtName;
    private TextView txtWarning , txtLogin , txtLicense;
    private ImageView firstImage ;
    private Button btnRegister;

    private  String image_url;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        databaseHelper =  new DatabaseHelper(this);

        image_url ="first";

        handleImageUrl();
        btnRegister.setOnClickListener( v ->{

            initRegister();

        });


    }

    private void handleImageUrl() {

        Log.d(TAG, "handleImageUrl: started");
        firstImage.setOnClickListener( v->{
            image_url = "first";


        });
    }

    private void initRegister() {

        Log.d(TAG, "initRegister: started");
        String email = edtTxtEmail.getText().toString();
        String password = edtTtPassword.getText().toString();

        if(email.equals("") || password.equals("")){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Please enter password or email");
        }else{
            txtWarning.setVisibility(View.GONE);


        }
    }

    private class DoesUserExists extends AsyncTask<String , Void , Boolean>{


        @Override
        protected Boolean doInBackground(String... strings) {
            try{

                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                Cursor cursor = db.query("users" , new String[]{"_id" , "email"} , "email=?" , new String[]{strings[0]} , null , null , null);
                if( null != cursor){
                    if(cursor.moveToFirst()){

                        if(cursor.getString(cursor.getColumnIndex("email")).equals(strings[0])){

                            cursor.close();
                            db.close();
                            return true;
                        }else{
                            cursor.close();
                            db.close();
                            return false;
                        }

                    }else{

                        cursor.close();
                        db.close();
                        return false;
                    }
                }else{
                    db.close();

                    return true;
                }

            }catch (SQLException e){
                e.printStackTrace();

                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("There is user with this email please try another email");
            }else{
                txtWarning.setVisibility(View.GONE);

            }
        }
    }

    private class RegisterUser extends AsyncTask<Void , Void , User>{

        private String email , password , address , first_name , last_name;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             this.email = edtTxtEmail.getText().toString();
             this.password = edtTtPassword.getText().toString();
            this.address = edtTxtAddress.getText().toString();

            String name = edtTxtName.getText().toString();

            String [] names =  name.split(" ");

            if( names.length >= 1){
                first_name = names[0];
                for (int  i = 0;  i <  names.length;  i++) {
                    if( i > 1){
                        last_name +=" " + names[i];
                    }else{
                        last_name +=names[i];
                    }
                }

            }else{
                this.first_name = names[0];

            }

        }

        @Override
        protected User doInBackground(Void... voids) {

            try{

                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("email" , this.email);
                values.put("password" , this.password);
                values.put("address"  , this.address);
                values.put("first_name" , this.first_name);
                values.put("last_name" , this.last_name);
                values.put("remained_amount" , 0.0);
                values.put("image_url" , image_url);

              long userId =   db.insert("users" , null , values);

                Log.d(TAG, "doInBackground: userId");
                Cursor cursor = db.query("users" , null ,"_id=?" , new String[]{ String.valueOf(userId)} , null , null , null);

                if(null != cursor) {
                    if(cursor.moveToFirst()){
                        User user =  new User();
                        user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                        user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                        user.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
                        user.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
                        user.setImage_url(cursor.getString(cursor.getColumnIndex("image_url")));
                        user.setRemained_amount(cursor.getDouble(cursor.getColumnIndex("remained_amount")));

                        cursor.close();
                        db.close();
                        return user;

                    }else{
                        cursor.close();
                        db.close();
                        return  null;

                    }

                }else{
                    db.close();
                    return null;
                }

            }catch (SQLException e){
                e.printStackTrace();

                return null;
            }

        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if(null != user){
                Toast.makeText(RegisterActivity.this, "User  Registered successfully", Toast.LENGTH_SHORT).show();

                Utils utils = new Utils(RegisterActivity.this);
                utils.addUserToSharedPreferences(user);
                Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }else{
                Toast.makeText(RegisterActivity.this, "not able to register please try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initViews() {

        Log.d(TAG, "initViews: started");

        edtTtPassword = findViewById(R.id.edttxtpassword);
        edtTxtAddress =  findViewById(R.id.edtTxtAddress);
        edtTxtEmail = findViewById(R.id.edttxtEmail);
        edtTxtName = findViewById(R.id.edtTxtName);

        txtWarning = findViewById(R.id.txtWarning);
        txtLogin =  findViewById(R.id.login);
        txtLicense = findViewById(R.id.txtLicense);
        btnRegister = findViewById(R.id.btnRegister);

        firstImage = findViewById(R.id.firstImage);
    }
}