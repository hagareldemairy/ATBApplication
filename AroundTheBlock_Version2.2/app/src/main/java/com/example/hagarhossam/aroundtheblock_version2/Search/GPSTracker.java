package com.example.hagarhossam.aroundtheblock_version2.Search;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.os.Bundle;

public class GPSTracker  extends Service implements LocationListener {



    private Context context = null;

    Location location;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BET_UPDATES = 1000 * 60 *1;

    protected LocationManager locationManager;


    public GPSTracker() {

    }

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public Location getLocation(){

        try {

            if (locationManager != null) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.removeUpdates(GPSTracker.this);
                }
            }

            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){

            }

            else{

                this.canGetLocation = true;

                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(
                            locationManager.NETWORK_PROVIDER, MIN_TIME_BET_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                    if(locationManager != null) {

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }

                    }

                }

                if(isGPSEnabled){

                    if(location == null){

                        locationManager.requestLocationUpdates(
                                locationManager.GPS_PROVIDER,
                                MIN_TIME_BET_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locationManager !=null){
                            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

                            if(location != null){

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }

                }

            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return  location;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void stopUsingGPS(){

        if (locationManager != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    public double getLatitude (){
        if(location != null ){
            latitude = location.getLatitude();
        }
        return latitude;
    }


    public double getLongitude(){
        if(location != null ){
            longitude = location.getLongitude();
        }
        return longitude;
    }


    public boolean canGetLocation(){

        return this.canGetLocation;
    }

    public void showSettingsAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setMessage("GPS is settings");

        alertDialog.setMessage("GPS is not enabled");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

