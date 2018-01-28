package com.mahendri.hargapasar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class AddKomoditiActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText locationText;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    AlertDialog permissionDialog;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String namaTempat = getIntent().getStringExtra("LOC_NAME");
        if (namaTempat == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_add_komoditi);
        locationText = findViewById(R.id.text_lat_lng);
        TextView tempatText = findViewById(R.id.text_tempat);
        FloatingActionButton fab = findViewById(R.id.add_komoditi_fab);

        tempatText.setText(namaTempat);
        fab.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            updateLocation();
        } else {
            // belum mendapat permission
            permissionDialog = new AlertDialog.Builder(this)
                    .setTitle("Lokasi Dibutuhkan")
                    .setMessage("Lokasi diperlukan untuk mendata komoditas")
                    .setNeutralButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create();
            permissionDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (permissionDialog.isShowing()) {
            permissionDialog.dismiss();
            finish();
        } else super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_komoditi_fab:
                if (longitude == 0.0d && latitude == 0.0d)
                    Toast.makeText(this, "lokasi kosong", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "lanjut ke simpan", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000); // update setiap lima detik

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                        locationText.setText(String.format(Locale.US,"%s, %s",
                                latitude, longitude));
                    }
                }
            };

            // init untuk pertama kalinya
            if (fusedLocationClient == null){
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
}
