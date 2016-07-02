package com.example.hagarhossam.aroundtheblock_version2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

    }

    public void onFeedbackSubmit (View view){

        Toast.makeText(this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
    }
}
