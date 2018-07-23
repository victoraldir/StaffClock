package com.quartzo.staffclock.utils;


import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.util.HashMap;

public class Constants {

    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 2;

    public static final HashMap<String, FirebaseVisionLatLng> LANDMARKS = new HashMap<String, FirebaseVisionLatLng>();
    static {
        // San Francisco International Airport.
        LANDMARKS.put("Close home", new FirebaseVisionLatLng( -23.2405,-45.9172));

        // Googleplex.
        LANDMARKS.put("Zone 2 close home", new FirebaseVisionLatLng(-23.24076,-45.91727));

        // Test
        LANDMARKS.put("Zone 3", new FirebaseVisionLatLng(-23.23877,-45.9168));
    }
}
