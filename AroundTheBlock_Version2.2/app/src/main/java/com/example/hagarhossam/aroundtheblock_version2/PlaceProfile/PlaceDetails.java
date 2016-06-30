package com.example.hagarhossam.aroundtheblock_version2.PlaceProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.ListView.Helper;
import com.example.hagarhossam.aroundtheblock_version2.NavigationMainActivity;
import com.example.hagarhossam.aroundtheblock_version2.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class PlaceDetails extends AppCompatActivity {

    Database db ;
    String email;
    String placeId;
    EditText mEdit;
    RatingBar rating_b; // Rating
    TextView placeName;
    TextView placeAddress;
    TextView placeNumber;
    ImageView placeImage;
    ArrayList placeDetails = new ArrayList();
    Button buttonSubmit;
    Button savePlace;
    ArrayList<String> ratingList;
    ArrayList<ArrayList<String>> newBigList;

    String result; // Save place





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        //get The arraylist from Searched places that have the details of the place
        Intent intent = getIntent();
        placeDetails = intent.getStringArrayListExtra("placeDetails");


        // get user ID from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        System.out.println("THE EMAIL"+email);


        rating_b = (RatingBar) findViewById(R.id.ratingBar);
        placeName = (TextView)findViewById(R.id.place_name);
        placeAddress = (TextView)findViewById(R.id.place_address);
        placeNumber = (TextView)findViewById(R.id.phoneNumber);
        placeImage = (ImageView)findViewById(R.id.imageView2);
        mEdit   = (EditText)findViewById(R.id.reviewIDText);
        buttonSubmit = (Button) findViewById(R.id.button);
        savePlace = (Button) findViewById(R.id.save_place);


        placeName.setText( placeDetails.get(1).toString() );
        placeAddress.setText( placeDetails.get(2).toString() );
        placeId = placeDetails.get(0).toString();

 //////////////////////// set mobile number ///////////////////////////////////////
        if (!placeDetails.get(3).toString().contains("null")) {
            placeNumber.setText("Mobile number: "+placeDetails.get(3).toString());
        }

        else {
            placeNumber.setVisibility(View.INVISIBLE);
        }

//////////////////////// end of set mobile number ///////////////////////////////////////

////////////////////////  set place image //////////////////////////////////////////////
        String imageURL = placeDetails.get(11).toString();

        System.out.println("THE URRL "+imageURL);

        if (imageURL.contains("http://")) {

            System.out.println("DA5LTT EL IF");
            Picasso.with(getApplicationContext()).load(imageURL).into(placeImage);
        }


////////////////////////  end of set place image //////////////////////////////////////////////

////////////////////////////////////////////////  SET LIST VIEW  ////////////////////////////////////////
        db = new Database();
        ArrayList<ArrayList<String>> BigList = new ArrayList<>();
        ratingList = new ArrayList<>();
        BigList = db.selectReviews(placeId);
        ratingList = db.selectRating(placeId);
        System.out.println("rating list isss... "+ratingList);

        newBigList = new ArrayList<>();

        if(ratingList.size() !=0)//y3ni feh rating w review, fa display them.. el condition da 3shan bydrb
        {
            for (int i = 0; i < BigList.size(); i++)
            {
                ArrayList<String> tempList = new ArrayList<>();

                for (int j = 0; j < BigList.get(i).size(); j++)
                {
                    tempList.add(BigList.get(i).get(j));
                }


                tempList.add(ratingList.get(i));

                newBigList.add(tempList);
            }
        }

        System.out.println("NEW Big list is "+newBigList);

        ListAdapter buckysAdaptor = new ReviewCustomAdaptor(this, newBigList);

        ListView buckysListView = (ListView) findViewById(R.id.listView);
        buckysListView.setAdapter(buckysAdaptor);
        Helper.getListViewSize(buckysListView);

        if (email == "") {

            mEdit.setEnabled(false);
            mEdit.setFocusable(false);
            mEdit.setHint("Sign in or Sign up to write your Review");
            buttonSubmit.setVisibility(View.INVISIBLE);
            savePlace.setVisibility(View.INVISIBLE);
        }


    }

    public void SubmitButtonClicked(View view)
    {
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        String review = mEdit.getText().toString();
        System.out.println("REVIEW "+review);


        if(String.valueOf(rating_b.getRating())!="0.0" && !review.equals("")){
            db.insertReview(email, placeId, review, formattedDate);
            Boolean result = db.addRating(email, placeId,String.valueOf(rating_b.getRating()));
        }

        else{
            Toast.makeText(PlaceDetails.this, "Rating and Review must be given", Toast.LENGTH_LONG).show();
        }
        finish();
        startActivity(getIntent());


    }

    public void onBackPressed() {
        Intent intent = new Intent(PlaceDetails.this, NavigationMainActivity.class);//mfrood mn place profile l place profile w 5alas kda 5eles el recommendation
        startActivity(intent);
    }

    public void onMapsButton(View view){

        Intent navigation = new Intent(this, MapsActivity.class);
        navigation.putStringArrayListExtra("placeDetails", placeDetails);
        startActivity(navigation);
    }

    public void onReportErrorButton(View view){

        Intent Error = new Intent(this, ReportPlaceError.class);
        Error.putExtra("PlaceID", placeId);
        startActivity(Error);
    }

    public void onSavePlaceButton(View view){

        result = db.savePlace(email,placeId);
        if(result.contains("Duplicate")){
            Toast.makeText(getBaseContext(), "Place is already saved", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getBaseContext(), "Place saved", Toast.LENGTH_LONG).show();

    }


}
