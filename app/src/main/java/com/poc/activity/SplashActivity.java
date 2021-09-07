package com.poc.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.poc.R;
import com.poc.utils.CommonUtils;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 199;
    private Activity activity;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }
            }
        }, 200);
    }

    //todo check required permissions
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != 0) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Read Call Logs, Read Contacts and Read SMS permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(activity, new String[]{
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
                    }
                });
                builder.setNeutralButton("Cancel", (DialogInterface.OnClickListener) null);
                builder.create().show();
                return;
            }
            ActivityCompat.requestPermissions(this.activity, new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        } else if (!CommonUtils.isLocationEnabled(this.activity)) {
            turnOnGpsLocation();
        } else {
            startActivity(new Intent(this.activity, MainActivity.class));
            finish();
        }
    }

    //todo check status(enable/disable) for GPS
    private void turnOnGpsLocation() {
        setFinishOnTouchOutside(true);
        LocationManager manager = (LocationManager) getSystemService("location");
        if (manager.isProviderEnabled("gps") && hasGPSDevice(this)) {
            Toast.makeText(this, "Gps already enabled", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this.activity, MainActivity.class));
            finish();
        }
        if (!hasGPSDevice(this)) {
            Toast.makeText(this, "Gps not Supported", Toast.LENGTH_LONG).show();
        }
        if (manager.isProviderEnabled("gps") || !hasGPSDevice(this)) {
            Log.e("TAG", "Gps already enabled");
            Toast.makeText(this, "Gps already enabled", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this.activity, MainActivity.class));
            finish();
            return;
        }
        Log.e("TAG", "Gps already enabled");
        Toast.makeText(this, "Gps not enabled", Toast.LENGTH_LONG).show();
        enableLoc();
    }


    //todo check whether device has GPS functionality
    private boolean hasGPSDevice(Context context) {
        List<String> providers;
        LocationManager mgr = (LocationManager) context.getSystemService("location");
        if (mgr == null || (providers = mgr.getAllProviders()) == null) {
            return false;
        }
        return providers.contains("gps");
    }


    //todo show dialog to enable GPS
    private void enableLoc() {
        if (this.googleApiClient == null) {
            GoogleApiClient build = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                public void onConnected(Bundle bundle) {
                }

                public void onConnectionSuspended(int i) {
                    SplashActivity.this.googleApiClient.connect();
                }
            }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                }
            }).build();
            this.googleApiClient = build;
            build.connect();
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(100);
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        LocationServices.SettingsApi.checkLocationSettings(this.googleApiClient, builder.build()).setResultCallback(new ResultCallback<LocationSettingsResult>() {
            public void onResult(LocationSettingsResult result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case 6:
                        try {
                            status.startResolutionForResult(SplashActivity.this, SplashActivity.REQUEST_LOCATION);
                            return;
                        } catch (IntentSender.SendIntentException e) {
                            return;
                        }
                    default:
                        return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length <= 0 || grantResults[0] +
                        grantResults[1] +
                        grantResults[2] +
                        grantResults[3] +
                        grantResults[4] != 0) {
                    Toast.makeText(this.activity, "Permissions denied.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!CommonUtils.isLocationEnabled(this.activity)) {
                    turnOnGpsLocation();
                    return;
                } else {
                    startActivity(new Intent(this.activity, MainActivity.class));
                    finish();
                    return;
                }
            default:
                return;
        }
    }
}