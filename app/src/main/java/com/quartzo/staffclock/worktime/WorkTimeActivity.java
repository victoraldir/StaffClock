package com.quartzo.staffclock.worktime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.quartzo.staffclock.R;
import com.quartzo.staffclock.ViewModelFactory;
import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.event.EventActivity;
import com.quartzo.staffclock.exceptions.DateTimeNotFoundException;
import com.quartzo.staffclock.geofence.GeofenceObserver;
import com.quartzo.staffclock.interfaces.Callbacks;
import com.quartzo.staffclock.textrecognition.LivePreviewActivity;
import com.quartzo.staffclock.utils.GeofenceUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.quartzo.staffclock.utils.Constants.RC_ACCESS_FINE_LOCATION;
import static com.quartzo.staffclock.utils.Constants.RC_PLACE_PICKER;

public class WorkTimeActivity extends AppCompatActivity implements
        Observer<List<Event>>,
        Callbacks.WorkTimeCallback,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Context mContext;
    private WorkTimeViewModel mEventViewModel;
    private RecyclerView mWorkTimeRecycle;
    private WorkTimeViewAdapter mAdapter;
    private GeofenceObserver mGeofenceObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mGeofenceObserver = new GeofenceObserver(this);
        getLifecycle().addObserver(mGeofenceObserver);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEventViewModel = ViewModelFactory.getInstance(getApplication()).create(WorkTimeViewModel.class);
        mEventViewModel.getListEvents().observe(this,this);

        mWorkTimeRecycle = findViewById(R.id.work_time_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mWorkTimeRecycle.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mWorkTimeRecycle.getContext(),
                linearLayoutManager.getOrientation());
        mWorkTimeRecycle.addItemDecoration(dividerItemDecoration);
        mAdapter = new WorkTimeViewAdapter(new ArrayList(), this);
        mWorkTimeRecycle.setAdapter(mAdapter);

        Button btnPickPlace = findViewById(R.id.btn_place_picker);
        btnPickPlace.setOnClickListener(v -> GeofenceUtils.launchPickActivity((Activity) mContext));

        com.github.clans.fab.FloatingActionButton fabCam = findViewById(R.id.fab_btn_camera);
        fabCam.setOnClickListener(view -> launchActivity());

        com.github.clans.fab.FloatingActionButton fabFinger = findViewById(R.id.fab_btn_finger);
        fabFinger.setOnClickListener(view -> launchDatePicker());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(mContext, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mGeofenceObserver.addGeofence(place);

            }
        }
    }

    private void launchDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                WorkTimeActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );

        // If you're calling this from an AppCompatActivity
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private void launchTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                WorkTimeActivity.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );

        // If you're calling this from an AppCompatActivity
        dpd.show(getSupportFragmentManager(), "Timepickerdialog");
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
                    GeofenceUtils.launchPickActivity(this);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Well, in this case we can't offer you the aproximated check time",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
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
                GeofenceUtils.launchPickActivity(this);
                break;
            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(@Nullable List<Event> eventList) {
        mAdapter.swapData(eventList);
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
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mEventViewModel.setTime(hourOfDay,minute,second);
        try{
            mEventViewModel.saveEvent();
            Toast.makeText(mContext,mEventViewModel.getDateTime() + " saved",Toast.LENGTH_SHORT).show();
        }catch (DateTimeNotFoundException ex){
            ex.printStackTrace();
            Toast.makeText(mContext,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        Toast.makeText(mContext,"year: " + year + "month: " + monthOfYear + "day: "+ dayOfMonth,Toast.LENGTH_SHORT).show();
        mEventViewModel.setDate(dayOfMonth,monthOfYear,year);
        launchTimePicker();
    }
}


