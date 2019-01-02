package com.quartzo.staffclock.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.quartzo.staffclock.GeofenceTransitionsIntentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GeofenceUtils {

    public static void launchPickActivity(Activity activity){

        if(checkPermission(activity)) {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                activity.startActivityForResult(builder.build(activity), Constants.RC_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity,"Permission not garanted",Toast.LENGTH_SHORT).show();
            requestPermission(activity);
            return false;
        }
        return true;
    }

    public static void requestPermission(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.RC_ACCESS_FINE_LOCATION);

    }

    public static void addGeofence(Activity activity, GoogleApiClient googleApiClient,
                                   GeofencingClient geofencingClient, Place place) {

        if(checkPermission(activity))

        if (!googleApiClient.isConnected()) {
            Toast.makeText(activity, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            geofencingClient.addGeofences(getGeofencingRequest(populateGeofenceList(place)), getGeofencePendingIntent(activity))
                    .addOnSuccessListener(aVoid -> Toast.makeText(activity, "Geofence added successfuly", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(activity, "Failure when adding Geofence. " + e.getMessage(), Toast.LENGTH_SHORT).show());

        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    public static void removeGeofence(Activity activity, GeofencingClient geofencingClient) {

        if(checkPermission(activity))

        geofencingClient.removeGeofences(getGeofencePendingIntent(activity))
                .addOnSuccessListener(activity, aVoid -> Toast.makeText(activity, "Geofence successfully removed", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(activity, e -> Toast.makeText(activity, "Geofence couldn't be removed", Toast.LENGTH_SHORT).show());
    }

    private static GeofencingRequest getGeofencingRequest(List geofenceList) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        if(!geofenceList.isEmpty())
            builder.addGeofences(geofenceList);

        return builder.build();
    }

    private static PendingIntent getGeofencePendingIntent(Activity activity) {
        // Reuse the PendingIntent if we already have it.
//        if (geofencePendingIntent != null) {
//            return geofencePendingIntent;
//        }
        Intent intent = new Intent(activity, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        PendingIntent geofencePendingIntent = PendingIntent.getService(activity, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    public static List<Geofence> populateGeofenceList(Place place) {
        return Collections.singletonList(buildGeofence(place));

    }

    public static Geofence buildGeofence(Place place){
        return new Geofence.Builder()
                .setRequestId(Constants.GEOFENCE_ID)
                .setCircularRegion(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }
}
