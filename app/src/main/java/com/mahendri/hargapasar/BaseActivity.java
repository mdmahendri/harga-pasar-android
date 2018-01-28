package com.mahendri.hargapasar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mahendri.hargapasar.entity.map.PlaceNearbyResponse;
import com.mahendri.hargapasar.entity.map.PlaceResult;
import com.mahendri.hargapasar.network.RetrofitConnect;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnSuccessListener<Location> {

    private static final int REQUEST_PERMISSION_LOCATION = 1;

    private View rootLayout;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        rootLayout = findViewById(R.id.root_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void addPasarMarker() {
        MarkerOptions pasarBonsay = new MarkerOptions()
                .position(new LatLng(-6.2307896, 106.8671854))
                .icon(bitmapDescriptorFromVector(this, R.drawable.marker_market))
                .title("Pasar Bonsay");

        map.addMarker(pasarBonsay);
    }

    @Override
    public void onSuccess(Location location) {
        if (location == null) {
            Timber.w("tidak dapat lokasi padahal setMyLocationEnabled true");
            return;
        }

        RetrofitConnect.retrieveNearby(location).enqueue(new Callback<PlaceNearbyResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceNearbyResponse> call,
                                   @NonNull Response<PlaceNearbyResponse> response) {
                if (!response.isSuccessful()) {
                    Timber.w("respon error");
                    return;
                }

                PlaceNearbyResponse nearbyResponse = response.body();
                Timber.i("Place name: %s", nearbyResponse.getStatus());

                List<PlaceResult> placeResults = nearbyResponse.getResults();
                for (PlaceResult place : placeResults) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(place.getLocation())
                            .icon(bitmapDescriptorFromVector(BaseActivity.this, R.drawable.marker_market))
                            .title(place.getName());
                    map.addMarker(marker);

                }

            }

            @Override
            public void onFailure(@NonNull Call<PlaceNearbyResponse> call, @NonNull Throwable t) {
                Timber.e(t);
            }
        });
    }
}
