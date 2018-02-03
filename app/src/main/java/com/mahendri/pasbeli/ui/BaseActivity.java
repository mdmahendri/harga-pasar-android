package com.mahendri.pasbeli.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mahendri.pasbeli.R;
import com.mahendri.pasbeli.entity.map.PlaceResult;
import com.mahendri.pasbeli.ui.harga.AddKomoditiActivity;
import com.mahendri.pasbeli.ui.harga.DataHistoryActivity;
import com.mahendri.pasbeli.util.VectorBitmapConvert;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnSuccessListener<Location>,
        GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private static final int REQUEST_PERMISSION_LOCATION = 1;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    MapViewModel mapViewModel;

    private View rootLayout;
    private TextView titleText;
    private TextView locationText;
    private TextView distanceText;
    private BottomSheetBehavior bottomSheetBehavior;

    private Location currentLocation;
    private GoogleMap map;
    private PlaceResult selectedPlace;
    private HashMap<String, PlaceResult> placeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_base);

        rootLayout = findViewById(R.id.root_layout);
        View sheetLayout = findViewById(R.id.bottom_sheet);
        titleText = sheetLayout.findViewById(R.id.place_title);
        locationText = sheetLayout.findViewById(R.id.place_address);
        distanceText = sheetLayout.findViewById(R.id.place_distance);
        Button listKomoditasBtn = sheetLayout.findViewById(R.id.place_button);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        bottomSheetBehavior = BottomSheetBehavior.from(sheetLayout);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        listKomoditasBtn.setOnClickListener(this);
        mapFragment.getMapAsync(this);

        mapViewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_history:
                startActivity(new Intent(this, DataHistoryActivity.class));
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

            Snackbar.make(rootLayout, "Dibutuhkan akses lokasi untuk mendata komoditas",
                    Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // meminta permission
                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSION_LOCATION);
                }
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

            map.setMyLocationEnabled(true);

            // get lokasi sekarang
            LocationServices.getFusedLocationProviderClient(this)
                    .getLastLocation()
                    .addOnSuccessListener(this, this);

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

        List<PlaceResult> placeResults = mapViewModel.getMapNearby(currentLocation);
        placeMap = new HashMap<>(placeResults.size());
        for (PlaceResult place : placeResults) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(place.getLocation())
                    .icon(VectorBitmapConvert.fromVector(BaseActivity.this,
                            R.drawable.marker_market))
                    .title(place.getName());
            Marker marker = map.addMarker(markerOptions);
            placeMap.put(marker.getId(), place);
        }

        // set listener klik pada marker
        map.setOnMarkerClickListener(BaseActivity.this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String uuid = marker.getId();
        selectedPlace = placeMap.get(uuid);
        setSheetValue(selectedPlace);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.place_button:
                Intent toAdd = new Intent(this, AddKomoditiActivity.class);
                toAdd.putExtra("LOC_NAME", selectedPlace.getName());
                startActivity(toAdd);
                break;
        }
    }

    private void setSheetValue(PlaceResult place) {

        // tidak tercatat lokasi sekarang
        if (currentLocation == null) {
            checkMyLocationLayer();
            return;
        }

        // konversi LatLng ke Location
        LatLng latLng = place.getLocation();
        Location loc = new Location("");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        float distance = currentLocation.distanceTo(loc) / 1000;

        // pembulatan distance
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        // bind text ke view
        titleText.setText(place.getName());
        locationText.setText(place.getVicinity());
        distanceText.setText(String.format("%s KM", decimalFormat.format(distance)));
    }
}
