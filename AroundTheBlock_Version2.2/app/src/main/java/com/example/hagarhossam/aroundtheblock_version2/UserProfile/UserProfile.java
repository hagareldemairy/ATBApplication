package com.example.hagarhossam.aroundtheblock_version2.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.NavigationMainActivity;
import com.example.hagarhossam.aroundtheblock_version2.PlaceProfile.PlaceDetails;
import com.example.hagarhossam.aroundtheblock_version2.R;

import java.util.ArrayList;


public class UserProfile extends ActionBarActivity {


    Database db ;
    TextView textView; // Username text
    ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        db = new Database();

        // Intent intent = getIntent();
        //String username = intent.getStringExtra(SignIn.USER_NAME);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String user_name = sharedPreferences.getString("name", "");
        final String email = sharedPreferences.getString("email", "");

        textView = (TextView) findViewById(R.id.txtEmail);
        textView.setText( user_name );

        profileImage = (ImageView) findViewById(R.id.imageView);
        profileImage.setImageResource(R.drawable.usersss);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public void onMyPlacesButtonClick(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", "");
        ArrayList<ArrayList<String>> BigList = new ArrayList<>();

        BigList = db.selectSavedPlaces(email);
        if(BigList.size()>=1){
            Intent myPlaces = new Intent(this, SavedPlace.class);
            startActivity(myPlaces);
        }

        else{
            Toast.makeText(this, "No Saved Places", Toast.LENGTH_LONG).show();
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void onLogout(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
        Intent logoutIntent = new Intent(this, NavigationMainActivity.class);
        startActivity(logoutIntent);

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void onEditProfileButtonClick(View view){
        Intent editProfile = new Intent(this, EditProfile.class);
        startActivity(editProfile);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void onBackPressed() {
        Intent intent = new Intent(UserProfile.this, NavigationMainActivity.class);//mfrood mn place profile l place profile w 5alas kda 5eles el recommendation
        startActivity(intent);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void onDeleteAccount(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        final String email = sharedPreferences.getString("email", "");
        editor.clear();
        editor.commit();
        String result = db.DeleteAccount(email);
        Toast.makeText(this, "Account is deleted", Toast.LENGTH_LONG).show();
        Intent DeleteAcoountIntent = new Intent(this, NavigationMainActivity.class);
        startActivity(DeleteAcoountIntent);


    }
}

