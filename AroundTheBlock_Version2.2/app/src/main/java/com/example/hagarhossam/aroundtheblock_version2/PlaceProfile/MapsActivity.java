package com.example.hagarhossam.aroundtheblock_version2.PlaceProfile;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Database db = new Database();

        ArrayList placeDetails = new ArrayList();
        Intent intent = getIntent();
        placeDetails = intent.getStringArrayListExtra("placeDetails");
        placeId = placeDetails.get(0).toString();
        System.out.println("THE PLACE ID"+placeId);

        ArrayList<String> details = new ArrayList();
        details = db.Navigation(placeId);

        System.out.println("the lista isssssssssssssssssssssssss hna "+details);

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(details.get(1)), Double.parseDouble(details.get(2)));
        //LatLng sydney = new LatLng(30.0638,31.2205);
        mMap.addMarker(new MarkerOptions().position(sydney).title(details.get(0)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
