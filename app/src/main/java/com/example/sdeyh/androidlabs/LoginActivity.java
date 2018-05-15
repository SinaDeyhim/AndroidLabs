package com.example.sdeyh.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "LoginActivity";
    protected EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME,"In onCreate()");
        // grabbing the email EditText filed
        email = (EditText) findViewById(R.id.editTextEmail);
        displayEmail();

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");

    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");

    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");

    }
// saving the email address in sharedPref
    public void saveInfo(View view) {
        SharedPreferences sharedPref = getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPref.edit();
        EditText edit = (EditText)findViewById(R.id.editTextEmail);
        editor.putString("DefaultEmail", edit.getText().toString());
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);




    }
// method for displaying the saved email as the default value
    public void displayEmail(){
        SharedPreferences sharedPref = getSharedPreferences("info", Context.MODE_PRIVATE);
        String savedEmail = sharedPref.getString("DefaultEmail", "email@domain.com");
        email.setText(savedEmail);
    }

}
