package com.mahendri.pasbeli.ui.harga;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.mahendri.pasbeli.R;
import com.mahendri.pasbeli.entity.HargaKonsumen;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class AddKomoditiActivity extends AppCompatActivity implements View.OnClickListener {

    private LocationCallback locationCallback;

    private AutoCompleteTextView namaText;
    private TextInputEditText hargaText;
    private TextInputEditText namaTempatText;
    private TextInputEditText locationText;

    AlertDialog permissionDialog;
    private double longitude;
    private double latitude;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    FusedLocationProviderClient fusedLocationClient;

    HargaViewModel hargaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        String namaTempat = getIntent().getStringExtra("LOC_NAME");
        if (namaTempat == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_add_komoditi);
        namaText = findViewById(R.id.text_nama_barang);
        hargaText = findViewById(R.id.text_harga);
        namaTempatText = findViewById(R.id.text_nama_tempat);
        locationText = findViewById(R.id.text_lat_lng);
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
        FloatingActionButton fab = findViewById(R.id.add_komoditi_fab);

        namaTempatText.setText(namaTempat);
        fab.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            updateLocation();
        } else {
            // belum mendapat permission
            permissionDialog.show();
        }

        hargaViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(HargaViewModel.class);
        initAutoComplete();
    }

    private void initAutoComplete() {
        namaText.setAdapter();
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
                else {
                    insertDataBaru();
                    finish();
                }
                break;
        }
    }

    private void insertDataBaru() {
        if (TextUtils.isEmpty(namaText.getText()) || TextUtils.isEmpty(hargaText.getText())
                || TextUtils.isEmpty(locationText.getText())) {
            Toast.makeText(this, "Tidak boleh ada isian kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        HargaKonsumen hargaKonsumen = new HargaKonsumen();
        hargaKonsumen.namaBarang = namaText.getText().toString();
        hargaKonsumen.hargaBarang = Long.parseLong(hargaText.getText().toString());
        hargaKonsumen.namaTempat = namaTempatText.getText().toString();
        hargaKonsumen.latitude = latitude;
        hargaKonsumen.longitude = longitude;
        hargaKonsumen.waktuCatat = System.currentTimeMillis();

        hargaViewModel.insertNewHargaKomoditas(hargaKonsumen);
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
                    Location lastLocation = locationResult.getLastLocation();
                    longitude = lastLocation.getLongitude();
                    latitude = lastLocation.getLatitude();

                    locationText.setText(String.format(Locale.getDefault(),"%s, %s",
                            latitude, longitude));
                }
            };

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
}
