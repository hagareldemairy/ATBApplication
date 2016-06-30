package com.example.hagarhossam.aroundtheblock_version2.UserProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.R;

public class EditProfile extends AppCompatActivity {
    EditText _nameText;
    EditText _passwordText;
    Database db = new Database();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        _nameText = (EditText)findViewById(R.id.editText2);
        _passwordText = (EditText)findViewById(R.id.editText3);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String user_name = sharedPreferences.getString("name", "");
        String user_password = sharedPreferences.getString("password", "");

        _nameText.setText(user_name, TextView.BufferType.EDITABLE);
        _passwordText.setText(user_password, TextView.BufferType.EDITABLE);



    }

    public void onEditSubmit(View view) {

        String new_name = _nameText.getText().toString();
        String new_password=_passwordText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Boolean response = db.updateProfile(email,new_name,new_password);

        System.out.println("THE RESPONSE1111"+response);


        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show();



    }
}
