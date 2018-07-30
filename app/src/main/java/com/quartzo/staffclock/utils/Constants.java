package com.quartzo.staffclock.utils;


import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.util.HashMap;

public class Constants {

    public static final String TYPE_REAL = "REAL";
    public static final String TYPE_GEOFENCE = "GEOFENCE";

    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 500;

    public static final HashMap<String, FirebaseVisionLatLng> LANDMARKS = new HashMap<String, FirebaseVisionLatLng>();
    static {
//        // Home
//        LANDMARKS.put("Home", new FirebaseVisionLatLng( -23.2405,-45.9172));

        // Embraer
        LANDMARKS.put("Embraer", new FirebaseVisionLatLng(-23.22439,-45.85764));
    }
}
