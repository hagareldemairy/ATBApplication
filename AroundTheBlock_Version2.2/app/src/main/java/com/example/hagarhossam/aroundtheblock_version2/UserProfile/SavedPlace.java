package com.example.hagarhossam.aroundtheblock_version2.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.PlaceProfile.PlaceDetails;
import com.example.hagarhossam.aroundtheblock_version2.R;
import com.example.hagarhossam.aroundtheblock_version2.Search.SearchedPlaces;

import java.util.ArrayList;

public class SavedPlace extends AppCompatActivity {

    Database db ;
    String email;
    ListView _savedPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_place);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", "");
        db = new Database();
        _savedPlaces = (ListView)findViewById(R.id.listView);


        ArrayList<ArrayList<String>> BigList = new ArrayList<>();

        BigList = db.selectSavedPlaces(email);
        System.out.println("Big list is "+BigList);

        if(BigList.size()==0){

        }

        ListAdapter buckysAdaptor = new placeCustomAdaptor(this, BigList);
        System.out.println("LIST ABAPTER is "+buckysAdaptor);

        ListView buckysListView = (ListView) findViewById(R.id.listView);
        buckysListView.setAdapter(buckysAdaptor);




        //////////////////////////////////Select place from List /////////////////////////////////


        ArrayList tempList = new ArrayList();
        for(int i=0;i<BigList.size();i++)
        {
            tempList.add(BigList.get(i).get(0));
        }

        //templist di arraylist feha 3 elements eli homa el names beto3 el places recommended

        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tempList);

        _savedPlaces.setAdapter(adapter2);

        _savedPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList = (String) (_savedPlaces.getItemAtPosition(myItemInt));

                System.out.println("selected item in listview is " + selectedFromList);

                ArrayList<String> placeDetails = new ArrayList<String>();
                placeDetails = db.SelectPlaceDetailsGivenName(selectedFromList);

                Intent intent = new Intent(SavedPlace.this, PlaceDetails.class);//mfrood mn place profile l place profile w 5alas kda 5eles el recommendation
                intent.putStringArrayListExtra("placeDetails", placeDetails);
                intent.putExtra("userId", email); // w hnak fil place profile page m7tag nest2blo b intent tnya

                startActivity(intent);

            }
        });

    }


}

