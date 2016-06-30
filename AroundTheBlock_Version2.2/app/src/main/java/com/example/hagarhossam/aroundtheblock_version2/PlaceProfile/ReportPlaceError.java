package com.example.hagarhossam.aroundtheblock_version2.PlaceProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.R;

public class ReportPlaceError extends AppCompatActivity {

    EditText _placeIdText;
    EditText _ErrorDescriptionText;
    String PlaceId="";
    String Result="";
    Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_place_error);
        _ErrorDescriptionText = (EditText)findViewById(R.id.ErrorText);
        db= new Database();

    }

    public void onErrorSubmitButtonClicked(View view){
        Intent intent= getIntent();
        String ErrorDescription=_ErrorDescriptionText.getText().toString();
        if(ErrorDescription == ""){
            Toast.makeText(getBaseContext(), "Please enter a valid problem", Toast.LENGTH_LONG).show();
        }

        else{
            PlaceId = intent.getStringExtra("PlaceID");
            Result=db.ReportPlaceError(PlaceId, ErrorDescription);
            Toast.makeText(getBaseContext(), "Your report has been submitted", Toast.LENGTH_LONG).show();
        }

    }
}
