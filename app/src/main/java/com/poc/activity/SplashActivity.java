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
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.poc.R;
import com.poc.utils.CommonUtils;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 199;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        getSupportActionBar().hide();

        new Handler().postDelayed(() -> {

            if (Build.VERSION.SDK_INT >= 23) {
                checkPermission();
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
                builder.setMessage(R.string.list_of_required_permissions);
                builder.setTitle(R.string.grant_permissions);
                builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 123));
                builder.setNeutralButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                builder.create().show();
                return;
            }
            ActivityCompat.requestPermissions(this.activity, new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        } else {
            if (!CommonUtils.isLocationEnabled(activity)) {
                turnOnGpsLocation();
            } else {
                startActivity(new Intent(activity, MainActivity.class));
                finish();
            }
        }
    }

    //todo check status(enable/disable) for GPS
    private void turnOnGpsLocation() {
        setFinishOnTouchOutside(true);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled("gps") && hasGPSDevice(this)) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }
        if (!hasGPSDevice(this)) {
            Toast.makeText(this, activity.getResources().getString(R.string.gps_not_support), Toast.LENGTH_LONG).show();
        }
        if (manager.isProviderEnabled("gps") || !hasGPSDevice(this)) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
            return;
        }
        enableLoc();
    }


    //todo check whether device has GPS functionality
    private boolean hasGPSDevice(Context context) {
        List<String> providers;
        LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null || (providers = mgr.getAllProviders()) == null) {
            return false;
        }
        return providers.contains("gps");
    }


    //todo show dialog to enable GPS
    private void enableLoc() {

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    // startUpdatingLocation(...);
                })
                .addOnFailureListener(this, ex -> {
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(activity, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == 0) {
                Toast.makeText(this, activity.getResources().getString(R.string.enable_gps), Toast.LENGTH_LONG).show();
            } else {
            }
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
                    Toast.makeText(activity, activity.getResources().getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                } else {
                    if (!CommonUtils.isLocationEnabled(activity))
                        turnOnGpsLocation();
                    else {
                        startActivity(new Intent(activity, MainActivity.class));
                        finish();
                    }
                }
                return;
            default:
                return;
        }
    }
}