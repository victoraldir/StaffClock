package com.quartzo.staffclock.geofence;


import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.util.HashMap;

public class Constants {

    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 1;

    public static final HashMap<String, FirebaseVisionLatLng> LANDMARKS = new HashMap<String, FirebaseVisionLatLng>();
    static {
        // San Francisco International Airport.
        LANDMARKS.put("Moscone South", new FirebaseVisionLatLng(37.783888,-122.4009012));

        // Googleplex.
        LANDMARKS.put("Japantown", new FirebaseVisionLatLng(37.785281,-122.4296384));

        // Test
        LANDMARKS.put("SFO", new FirebaseVisionLatLng(37.621313,-122.378955));
    }
}
