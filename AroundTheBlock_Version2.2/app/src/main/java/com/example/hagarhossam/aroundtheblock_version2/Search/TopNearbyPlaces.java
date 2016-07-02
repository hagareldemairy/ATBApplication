package com.example.hagarhossam.aroundtheblock_version2.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.PlaceProfile.PlaceDetails;
import com.example.hagarhossam.aroundtheblock_version2.R;

import java.util.ArrayList;

public class TopNearbyPlaces extends AppCompatActivity {

    GPSTracker gps;
    Database db;
    Database db1;
    String latitude;
    String longitude;
    ArrayList<String> nearbyPlaces;
    ArrayList<ArrayList<String>> TopRatednearbyPlaces;
    ListView _nearbyList;
    String place1;
    String place2;
    String place3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_nearby_places);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", "");

        ////////  Initialization  ////////

        db = new Database();
        db1 = new Database();
        nearbyPlaces = new ArrayList<String>();
        TopRatednearbyPlaces = new ArrayList<ArrayList<String>>();
        gps = new GPSTracker(TopNearbyPlaces.this);
        _nearbyList = (ListView)findViewById(R.id.topNearbyPlaces);

        //////////////////////////////////

        if(gps.canGetLocation()){

            latitude = Double.toString (gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());

            //////////////////////////////////Search for the nearby places /////////////////////////////////

            nearbyPlaces = db.topNearbyPlaces(latitude, longitude);

            System.out.println("NEARBY PLACES " + nearbyPlaces);

            place1= nearbyPlaces.get(0).toString();
            place2= nearbyPlaces.get(1).toString();
            place3= nearbyPlaces.get(2).toString();



            TopRatednearbyPlaces = db.topRatedNearbyPlaces(place1, place2, place3);
            System.out.println("TOP NEARBY PLACES " + TopRatednearbyPlaces);


            if (nearbyPlaces.size() == 0) {

            }

            if (TopRatednearbyPlaces.size() == 0) {

            }

            ListAdapter buckysAdaptor = new SearchCustomAdaptor(this, TopRatednearbyPlaces);
            System.out.println("LIST ABAPTER is " + buckysAdaptor);

            ListView buckysListView = (ListView) findViewById(R.id.topNearbyPlaces);
            buckysListView.setAdapter(buckysAdaptor);

        }

        else{
            gps.showSettingsAlert();

        }




        //////////////////////////////////Select place from List /////////////////////////////////

        ArrayList tempList = new ArrayList();
        for (int i = 0; i < TopRatednearbyPlaces.size(); i++) {
            tempList.add(TopRatednearbyPlaces.get(i).get(0));
        }

        //templist di arraylist feha 3 elements eli homa el names beto3 el places recommended

        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tempList);

        _nearbyList.setAdapter(adapter2);

        _nearbyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList = (String) (_nearbyList.getItemAtPosition(myItemInt));

                System.out.println("selected item in listview is " + selectedFromList);

                ArrayList<String> placeDetails = new ArrayList<String>();
                placeDetails = db.SelectPlaceDetailsGivenName(selectedFromList);

                Intent intent = new Intent(TopNearbyPlaces.this, PlaceDetails.class);//mfrood mn place profile l place profile w 5alas kda 5eles el recommendation
                intent.putStringArrayListExtra("placeDetails", placeDetails);
                intent.putExtra("userId", email); // w hnak fil place profile page m7tag nest2blo b intent tnya

                startActivity(intent);

            }
        });

    }
}
