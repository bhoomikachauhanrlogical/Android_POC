package com.poc.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;
import com.poc.R;
import com.poc.adapter.TabLayoutAdapter;
import com.poc.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Activity activity;
    private List<Address> addresses;
    private ActivityMainBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3700B3")));

        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this.activity, Locale.getDefault());
        addViewPagerTabs();
    }


    //todo location callback to get lat/long and address
    LocationCallback mLocationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult) {
            Iterator<Location> it = locationResult.getLocations().iterator();
            while (it.hasNext()) {
                Location location = it.next();
                Log.e("MainActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                if (location == null) {
                    location = locationResult.getLastLocation();
                }
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String countryName = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String featureName = addresses.get(0).getFeatureName();
                    binding.txtCurrentAddress.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\n" + address + "\n" + city + "\n" + state);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    //todo add tabs into viewpager
    private void addViewPagerTabs() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getResources().getString(R.string.contacts)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getResources().getString(R.string.call)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getResources().getString(R.string.messages)));
        binding.tabLayout.setTabGravity(0);

        TabLayoutAdapter adapter = new TabLayoutAdapter(this, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fusedLocationProviderClient != null) {
            getLocationDetails();
        }
    }


    //todo add properties for location request
    private void getLocationDetails() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(102);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == 0) {
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }
}