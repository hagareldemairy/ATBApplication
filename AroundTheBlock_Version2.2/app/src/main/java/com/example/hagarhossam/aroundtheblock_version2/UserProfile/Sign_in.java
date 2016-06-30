package com.example.hagarhossam.aroundtheblock_version2.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.R;


public class Sign_in extends AppCompatActivity {

    EditText _editEmail, _editPassword;
    Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        _editEmail = (EditText)findViewById(R.id.edt_email);
        _editPassword = (EditText)findViewById(R.id.edt_pass);
    }

    public void onLogin(View view) {

        final String username = _editEmail.getText().toString();
        String password = _editPassword.getText().toString();
        String result = db.SignIn(username, password);

        if(result.length()>=1){

            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", username);
            editor.putString("password", password);
            editor.putString("name",result);
            editor.apply();

            Toast.makeText(this, "Signed in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Sign_in.this, UserProfile.class);
            startActivity(intent);
        }

        else {

            Toast.makeText(this, "Wrong email or password", Toast.LENGTH_LONG).show();
        }



    }
}
