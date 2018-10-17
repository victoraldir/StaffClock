package com.quartzo.staffclock.event;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quartzo.staffclock.R;
import com.quartzo.staffclock.ViewModelFactory;
import com.quartzo.staffclock.data.Event;
import com.quartzo.staffclock.interfaces.Callbacks;
import com.quartzo.staffclock.utils.DateUtils;

public class EventActivity extends AppCompatActivity implements Callbacks.EventCallback {

    public static final String ARG_DATE = ".date";
    public static final String ARG_TIME = ".time";
    private String mDateArgument;
    private String mTimeArgument;
    private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private static final String[] TABS = {"REAL","GEOFENCE"};
    private EventViewModel mEventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mEventViewModel = ViewModelFactory.getInstance(getApplication()).create(EventViewModel.class);

        mDateArgument = getIntent().getExtras().getString(ARG_DATE);
        mTimeArgument = getIntent().getExtras().getString(ARG_TIME);

        getSupportActionBar().setTitle(DateUtils.formatDateDDMMYYY(mDateArgument));
        getSupportActionBar().setSubtitle(mTimeArgument + " worked");

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLongEventClicked(Event event) {
        Toast.makeText(this,"Should delete event: " + event.toString(), Toast.LENGTH_SHORT).show();
        mEventViewModel.deleteEvent(event);
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new EventFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putString(EventFragment.ARG_DATE, mDateArgument);
            args.putString(EventFragment.ARG_TIME, mTimeArgument);
            args.putString(EventFragment.ARG_TYPE, TABS[i]);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return TABS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TABS[position];
        }
    }

}
