package com.quartzo.staffclock.worktime;

import android.Manifest;
import android.app.PendingIntent;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.quartzo.staffclock.GeofenceTransitionsIntentService;
import com.quartzo.staffclock.R;
import com.quartzo.staffclock.ViewModelFactory;
import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.event.EventActivity;
import com.quartzo.staffclock.geofence.GeofenceErrorMessages;
import com.quartzo.staffclock.interfaces.Callbacks;
import com.quartzo.staffclock.textrecognition.LivePreviewActivity;
import com.quartzo.staffclock.utils.Constants;
import com.quartzo.staffclock.utils.ListUtils;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkTimeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>,
        Observer<List<Event>>,
        Callbacks.WorkTimeCallback,
        LocationListener {

    private static final int RC_ACCESS_FINE_LOCATION = 1;
    private static final int RC_PLACE_PICKER = 2;

    private GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent;
    protected GoogleApiClient mGoogleApiClient;
    protected ArrayList<Geofence> mGeofenceList;
    private Context mContext;
    private WorkTimeViewModel mEventViewModel;
    private RecyclerView mWorkTimeRecycle;
    private WorkTimeAdapter mAdapter;
    private Map<String, LocalTime> mLocalTimeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEventViewModel = ViewModelFactory.getInstance(getApplication()).create(WorkTimeViewModel.class);

        mEventViewModel.getListEvents().observe(this,this);

        mContext = this;

        mWorkTimeRecycle = (RecyclerView) findViewById(R.id.work_time_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mWorkTimeRecycle.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mWorkTimeRecycle.getContext(),
                linearLayoutManager.getOrientation());
        mWorkTimeRecycle.addItemDecoration(dividerItemDecoration);

        mLocalTimeMap = new HashMap<>();
        mAdapter = new WorkTimeAdapter(mLocalTimeMap, this);

        mWorkTimeRecycle.setAdapter(mAdapter);

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        // Get the geofences used. Geofence data is hard coded in this sample.
//        populateGeofenceList();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        Button btnAddGeofence = (Button) findViewById(R.id.btn_add_geofence);
        btnAddGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGeofencesButtonHandler();
            }
        });
        Button btnRemoveGeofence = (Button) findViewById(R.id.btn_remove_geofence);
        btnRemoveGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeGeofencesButtonHandler();
            }
        });

        Button btnPickPlace = (Button) findViewById(R.id.btn_place_picker);
        btnPickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchPickActivity();
            }
        });

        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(mContext, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                populateGeofenceList(place);
                removeGeofence();
                addGeofence();
            }
        }
    }

    public void launchPickActivity(){

        if(checkPermission()) {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), RC_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission not garanted",Toast.LENGTH_SHORT).show();
            requestPermission();
            return false;
        }
        return true;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RC_ACCESS_FINE_LOCATION);

    }

    public void populateGeofenceList(Place place) {

        mGeofenceList.clear();

        mGeofenceList.add(new Geofence.Builder()
                .setRequestId(Constants.GEOFENCE_ID)
                .setCircularRegion(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

//        for (Map.Entry<String, FirebaseVisionLatLng> entry : Constants.LANDMARKS.entrySet()) {
//            mGeofenceList.add(new Geofence.Builder()
//                    .setRequestId(entry.getKey())
//                    .setCircularRegion(
//                            entry.getValue().getLatitude(),
//                            entry.getValue().getLongitude(),
//                            Constants.GEOFENCE_RADIUS_IN_METERS
//                    )
//                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
//                            Geofence.GEOFENCE_TRANSITION_EXIT)
//                    .build());
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RC_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    addGeofence();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Well, in this we can't offer you the aproximated check time",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void launchActivity(){
        startActivity(new Intent(this, LivePreviewActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_pin:
                launchPickActivity();
                break;
            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addGeofencesButtonHandler() {

        if(checkPermission()){
            addGeofence();
        }

    }

    public void removeGeofencesButtonHandler() {
        if(checkPermission()){
            removeGeofence();
        }
    }

    private void addGeofence() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mContext, "Geofence added successfuly", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Failure when adding Geofence. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    private void removeGeofence() {
        mGeofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Geofence successfully removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Geofence couldn't be removed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this,"onConnected fired",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Toast.makeText(this,"onConnectionSuspended fired",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed fired",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
        }
    }

    @Override
    public void onChanged(@Nullable List<Event> eventList) {
        mLocalTimeMap = ListUtils.calculateWorkTime(eventList);
        mAdapter.swapData(mLocalTimeMap);
    }

    @Override
    public void onWorkTimeClicked(String date, String workedTime) {
        Bundle bundle = new Bundle();
        bundle.putString(EventActivity.ARG_DATE,date);
        bundle.putString(EventActivity.ARG_TIME,workedTime);
        Intent it = new Intent(this, EventActivity.class);
        it.putExtras(bundle);
        startActivity(it);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this,"Lat: " + location.getLatitude() + "Lng : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }
}


