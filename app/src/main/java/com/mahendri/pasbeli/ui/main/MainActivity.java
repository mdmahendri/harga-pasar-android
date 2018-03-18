package com.mahendri.pasbeli.ui.main;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import com.mahendri.pasbeli.R;
import com.mahendri.pasbeli.databinding.ActivityMainBinding;
import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.ui.addharga.AddHargaActivity;
import com.mahendri.pasbeli.ui.history.DataHistoryActivity;
import com.mahendri.pasbeli.util.VectorBitmapConvert;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnSuccessListener<Location>, GoogleMap.OnMarkerClickListener {

    private static final int REQUEST_PERMISSION_LOCATION = 1;
    private static final int PLACE_PICKER_REQUEST = 1;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    FusedLocationProviderClient fusedLocationClient;

    MainViewModel mainViewModel;
    ActivityMainBinding binding;

    private BottomSheetBehavior bottomSheetBehavior;
    private ProgressBar progressBar;

    private Location currentLocation;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        binding.setViewmodel(mainViewModel);

        setupGoogleMap();
        setupBinding(binding);
        setupViewSubscibe();
    }

    private void setupGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }

    private void setupBinding(ActivityMainBinding binding) {
        progressBar = binding.progressBar;

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setupViewSubscibe() {
        mainViewModel.intentAdd.observe(this, namaPasar -> {
            if (namaPasar == null) return;

            mainViewModel.intentAdd.setValue(null);
            Intent intent = new Intent(this, AddHargaActivity.class);
            intent.putExtra(AddHargaActivity.EXTRA_NAMA_PASAR, namaPasar);
            startActivity(intent);
        });


        mainViewModel.addPasar.observe(this, result -> {
            if (result == null) return;

            mainViewModel.addPasar.setValue(null);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        });


        mainViewModel.sendHarga.observe(this, result -> {
            if (result == null) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                mainViewModel.sendHarga.postValue(null);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupMarker() {
        mainViewModel.getMapNearby(currentLocation).observe(this, listResource -> {
            if (listResource == null || listResource.data == null) return;

            map.clear();
            List<Pasar> daftarPasar = listResource.data;
            for (Pasar pasar : daftarPasar) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(pasar.getLocation())
                        .icon(VectorBitmapConvert.fromVector(MainActivity.this,
                                R.drawable.marker_market))
                        .title(pasar.nama);
                Marker marker = map.addMarker(markerOptions);
                marker.setTag(pasar);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_history:
                startActivity(new Intent(this, DataHistoryActivity.class));
                return true;
            case R.id.send_data:
                mainViewModel.sendDataHarga();
                return true;
            case R.id.new_pasar:
                toPlacePicker();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        checkMyLocationLayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) checkMyLocationLayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                Place place = PlacePicker.getPlace(this, data);
                mainViewModel.addPasar(place);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_LOCATION) {

            // granted permission
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkMyLocationLayer();
            } else {
                // meminta permission lagi
                requestLocationPermission();
            }
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            Snackbar.make(binding.rootLayout, "Dibutuhkan akses lokasi untuk mendata barang",
                    Snackbar.LENGTH_SHORT).setAction("OK", view -> {
                        // meminta permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_PERMISSION_LOCATION);
                    }).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
        }
    }

    private void checkMyLocationLayer() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // tambahkan titik sekarang
            map.setMyLocationEnabled(true);

            // get lokasi sekarang
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, this);

        } else requestLocationPermission();
    }

    @Override
    public void onSuccess(Location location) {
        if (location == null) {
            Timber.w("tidak dapat lokasi padahal setMyLocationEnabled true");
            return;
        }

        currentLocation = location;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 15));

        // draw marker
        setupMarker();

        // set listener klik pada marker
        map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Pasar selectedPasar = (Pasar) marker.getTag();

        // lokasi sekarang dibutuhkan untuk menghitung jarak
        if (selectedPasar != null && currentLocation != null) {
            mainViewModel.openSheet(currentLocation, selectedPasar);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else
            checkMyLocationLayer();

        return false;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else super.onBackPressed();
    }

    private void toPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

}