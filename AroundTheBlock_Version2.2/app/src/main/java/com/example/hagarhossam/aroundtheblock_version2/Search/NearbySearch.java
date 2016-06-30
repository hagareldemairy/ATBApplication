package com.example.hagarhossam.aroundtheblock_version2.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
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
import java.util.logging.Handler;

public class NearbySearch extends ActionBarActivity {

    GPSTracker gps;
    Database db;
    String latitude;
    String longitude;
    ArrayList<ArrayList<String>> nearbyPlaces;
    String searchText="";
    ListView _nearbyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_search);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", "");

        ////////  Initialization  ////////

        db = new Database();
        nearbyPlaces = new ArrayList<ArrayList<String>>();
        gps = new GPSTracker(NearbySearch.this);

        Intent intent = getIntent();
        searchText = intent.getExtras().getString("searchText");

        _nearbyList = (ListView)findViewById(R.id.nearbyPlaces);

        //////////////////////////////////

        if(gps.canGetLocation()){

             latitude = Double.toString (gps.getLatitude());
             longitude = Double.toString(gps.getLongitude());

            Toast.makeText(getApplicationContext(),"latitude "+latitude + "longitude "+longitude,Toast.LENGTH_LONG).show();
        }

        else{
            gps.showSettingsAlert();

        }


        System.out.println("SEARCHH TEXT "+searchText);
        nearbyPlaces = db.searchNearby(latitude,longitude,searchText);

        ListAdapter buckysAdaptor = new SearchCustomAdaptor(this, nearbyPlaces);
        System.out.println("LIST ABAPTER is " + buckysAdaptor);

        ListView buckysListView = (ListView) findViewById(R.id.nearbyPlaces);
        buckysListView.setAdapter(buckysAdaptor);


        ArrayList tempList = new ArrayList();
        for (int i = 0; i < nearbyPlaces.size(); i++) {
            tempList.add(nearbyPlaces.get(i).get(0));
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

                Intent intent = new Intent(NearbySearch.this, PlaceDetails.class);//mfrood mn place profile l place profile w 5alas kda 5eles el recommendation
                intent.putStringArrayListExtra("placeDetails", placeDetails);
                intent.putExtra("userId", email); // w hnak fil place profile page m7tag nest2blo b intent tnya

                startActivity(intent);

            }
        });
    }
}
