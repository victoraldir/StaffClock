package com.quartzo.staffclock.geofence;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.quartzo.staffclock.utils.GeofenceUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import static androidx.lifecycle.Lifecycle.Event.ON_CREATE;
import static androidx.lifecycle.Lifecycle.Event.ON_PAUSE;
import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static androidx.lifecycle.Lifecycle.Event.ON_STOP;

public class GeofenceObserver implements LifecycleObserver,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private Context mContext;
    protected GoogleApiClient mGoogleApiClient;
    private GeofencingClient mGeofencingClient;

    public GeofenceObserver(Context context) {
        mContext = context;
    }

    @OnLifecycleEvent(ON_CREATE)
    void onCreate(){
        mGeofencingClient = LocationServices.getGeofencingClient(mContext);
        buildGoogleApiClient();
    }

    @OnLifecycleEvent(ON_START)
    void start() {
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
        }
    }

    @OnLifecycleEvent(ON_STOP)
    void stop() {
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @OnLifecycleEvent(ON_PAUSE)
    void pause(){
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
            fusedLocationProviderClient.removeLocationUpdates(new LocationCallback());
        }
    }

    // Kick off the request to build GoogleApiClient.
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(mContext,"onConnected fired",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
        Toast.makeText(mContext,"onConnectionSuspended fired",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mContext,"onConnectionFailed fired",Toast.LENGTH_SHORT).show();
    }

    public void addGeofence(Place place){
        GeofenceUtils.removeGeofence((Activity) mContext,mGeofencingClient);
        GeofenceUtils.addGeofence((Activity) mContext,mGoogleApiClient,mGeofencingClient,
                place);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(mContext,"Lat: " + location.getLatitude() + "Lng : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }
}
