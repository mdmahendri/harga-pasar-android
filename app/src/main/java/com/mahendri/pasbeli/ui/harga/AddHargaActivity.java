package com.mahendri.pasbeli.ui.harga;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.mahendri.pasbeli.R;
import com.mahendri.pasbeli.entity.FetchStatus;
import com.mahendri.pasbeli.util.AutoValidator;
import com.mahendri.pasbeli.util.TextChangeWatch;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class AddHargaActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_NAMA_PASAR = "NAMA_PASAR";

    private LocationCallback locationCallback;

    private AutoCompleteTextView namaText;
    private Spinner kualitasSpinner;
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

        String namaTempat = getIntent().getStringExtra(EXTRA_NAMA_PASAR);
        if (namaTempat == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_add_harga);
        namaText = findViewById(R.id.text_nama_barang);
        kualitasSpinner = findViewById(R.id.spinner_kualitas);
        hargaText = findViewById(R.id.text_harga);
        namaTempatText = findViewById(R.id.text_nama_tempat);
        locationText = findViewById(R.id.text_lat_lng);
        permissionDialog = new AlertDialog.Builder(this)
                .setTitle("Lokasi Dibutuhkan")
                .setMessage("Lokasi diperlukan untuk mendata komoditas")
                .setNeutralButton("Keluar", (dialogInterface, i) -> finish())
                .create();
        FloatingActionButton fab = findViewById(R.id.add_komoditi_fab);

        kualitasSpinner.setEnabled(false);
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
        initSpinner();
    }

    private void initAutoComplete() {

        namaText.addTextChangedListener(new TextChangeWatch() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hargaViewModel.changeKualitas(charSequence.toString());
            }
        });

        hargaViewModel.getListBarang().observe(this, listResource -> {
            if (listResource == null || listResource.fetchStatus == FetchStatus.LOADING) {
                // empty implementation
            } else if (listResource.fetchStatus == FetchStatus.ERROR) {
                Toast.makeText(this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                finish();
            } else if (listResource.data != null && listResource.data.size() != 0) {
                namaText.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, listResource.data));
                namaText.setValidator(new AutoValidator(listResource.data));
                namaText.setOnFocusChangeListener((view, focus) -> {
                    if (!focus && view.getId() == namaText.getId()) namaText.performValidation();
                });
            }
        });
    }

    private void initSpinner() {
        kualitasSpinner.setOnItemSelectedListener(this);

        hargaViewModel.getKualitas().observe(this, listString -> {
            if (listString != null && listString.size() != 0) {
                kualitasSpinner.setEnabled(true);
                kualitasSpinner.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, listString));
            } else
                kualitasSpinner.setAdapter(null);
        });
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

        hargaViewModel.insertNewHarga(Long.parseLong(hargaText.getText().toString()),
                namaTempatText.getText().toString(), latitude, longitude);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selected = adapterView.getItemAtPosition(position).toString();
        hargaViewModel.selectBarang(selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
