package com.mahendri.pasbeli.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mahendri.pasbeli.R;
import com.mahendri.pasbeli.api.ApiResponse;
import com.mahendri.pasbeli.entity.FetchStatus;
import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.ui.harga.AddKomoditiActivity;
import com.mahendri.pasbeli.ui.harga.DataHistoryActivity;
import com.mahendri.pasbeli.util.DistanceConvert;
import com.mahendri.pasbeli.util.VectorBitmapConvert;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnSuccessListener<Location>,
        GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private static final int REQUEST_PERMISSION_LOCATION = 1;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ProgressBar progressBar;

    @Inject
    FusedLocationProviderClient fusedLocationClient;

    MapViewModel mapViewModel;

    private CoordinatorLayout rootLayout;
    private TextView titleText;
    private TextView locationText;
    private TextView distanceText;
    private BottomSheetBehavior bottomSheetBehavior;

    private Location currentLocation;
    private GoogleMap map;
    private Pasar selectedPasar;
    private HashMap<String, Pasar> placeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        setupProgressUpload();
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
                mapViewModel.sendDataHarga();
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

        mapViewModel.getMapNearby(currentLocation).observe(this, listResource -> {
            if (listResource != null && listResource.data != null) {
                List<Pasar> daftarPasar = listResource.data;
                placeMap = new HashMap<>(daftarPasar.size());
                for (Pasar pasar : daftarPasar) {
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(pasar.getLocation())
                            .icon(VectorBitmapConvert.fromVector(MainActivity.this,
                                    R.drawable.marker_market))
                            .title(pasar.nama);
                    Marker marker = map.addMarker(markerOptions);
                    placeMap.put(marker.getId(), pasar);
                }
            }
        });

        // set listener klik pada marker
        map.setOnMarkerClickListener(MainActivity.this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String uuid = marker.getId();
        selectedPasar = placeMap.get(uuid);
        setSheetValue(selectedPasar);

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
                toAdd.putExtra("LOC_NAME", selectedPasar.nama);
                startActivity(toAdd);
                break;
        }
    }

    private void setSheetValue(Pasar pasar) {

        // tidak tercatat lokasi sekarang
        if (currentLocation == null) {
            checkMyLocationLayer();
            return;
        }

        // bind text ke view
        titleText.setText(pasar.nama);
        locationText.setText(pasar.alamat);
        distanceText.setText(String.format("%s KM", DistanceConvert.toKm(currentLocation,
                pasar.getLocation())));
    }

    private void setupProgressUpload() {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(100, 100);
        params.gravity = Gravity.CENTER;
        rootLayout.addView(progressBar, params);

        mapViewModel.getUploadStatus().observe(this, stringResource -> {
            if (stringResource == null) return;

            FetchStatus fetchStatus = stringResource.fetchStatus;
            if (fetchStatus == FetchStatus.LOADING)
                progressBar.setVisibility(View.VISIBLE);
            else if (fetchStatus == FetchStatus.SUCCESS) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Sukses mengirim data",
                        Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Gagal mengirim data",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
