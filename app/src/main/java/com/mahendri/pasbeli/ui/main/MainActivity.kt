package com.mahendri.pasbeli.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener

import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.databinding.ActivityMainBinding
import com.mahendri.pasbeli.entity.Pasar
import com.mahendri.pasbeli.ui.addharga.AddHargaActivity
import com.mahendri.pasbeli.ui.history.DataHistoryActivity
import com.mahendri.pasbeli.ui.splash.SplashActivity
import com.mahendri.pasbeli.util.VectorBitmapConvert

import javax.inject.Inject

import dagger.android.AndroidInjection
import timber.log.Timber

class MainActivity : AppCompatActivity(), OnMapReadyCallback, OnSuccessListener<Location>,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    @Inject lateinit var signInClient: GoogleSignInClient
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var progressBar: ProgressBar
    private var map: GoogleMap? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewmodel = mainViewModel

        //check mail account
        title = intent.getStringExtra(SplashActivity.EXTRA_MAIL)

        //check Play Services
        val availability = GoogleApiAvailability.getInstance()
        val playStatus = availability.isGooglePlayServicesAvailable(this)
        if (playStatus == ConnectionResult.SUCCESS) {
            showView(binding)
        } else {
            availability.getErrorDialog(this, playStatus, FIX_PLAY_SERVICES_REQUEST).show()
        }
    }

    private fun showView(binding: ActivityMainBinding) {
        setupGoogleMap()
        setupBinding(binding)
        setupViewSubscibe()
    }

    private fun setupGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupBinding(binding: ActivityMainBinding) {
        progressBar = binding.progressBar

        bottomSheetBehavior = BottomSheetBehavior.from<RelativeLayout>(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupViewSubscibe() {
        mainViewModel.intentAdd.observe(this, Observer {
            if (it == null) return@Observer

            mainViewModel.intentAdd.value = null
            val intent = Intent(this, AddHargaActivity::class.java)
            intent.putExtra(AddHargaActivity.EXTRA_NAMA_PASAR, it)
            startActivity(intent)
        })

        mainViewModel.addPasar.observe(this, Observer {
            if (it == null) return@Observer

            mainViewModel.addPasar.value = null
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        })


        mainViewModel.sendHarga.observe(this, Observer {
            if (it == null) {
                progressBar.visibility = View.VISIBLE
            } else {
                mainViewModel.sendHarga.postValue(null)
                progressBar.visibility = View.GONE
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupMarker() {
        currentLocation?.let {
            mainViewModel.getMapNearby(it).observe(this, Observer {
                if (it?.data == null) return@Observer

                map?.clear()
                val daftarPasar = it.data
                for (pasar in daftarPasar) {
                    val markerOptions = MarkerOptions()
                            .position(pasar.location)
                            .icon(VectorBitmapConvert.fromVector(this@MainActivity,
                                    R.drawable.marker_market))
                            .title(pasar.nama)
                    val marker = map?.addMarker(markerOptions)
                    marker?.tag = pasar
                }
            })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_history -> {
                startActivity(Intent(this, DataHistoryActivity::class.java))
                return true
            }
            R.id.send_data -> {
                mainViewModel.sendDataHarga()
                return true
            }
            R.id.new_pasar -> {
                toPlacePicker()
                return true
            }
            R.id.sign_out -> {
                signInClient.signOut()
                        .addOnCompleteListener(this, {
                            startActivity(Intent(this, SplashActivity::class.java))
                            finish()
                        })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        checkMyLocationLayer()
    }

    override fun onResume() {
        super.onResume()
        checkMyLocationLayer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            PLACE_PICKER_REQUEST -> {
                val place = PlacePicker.getPlace(this, data)
                mainViewModel.addPasar(place)
            }
            FIX_PLAY_SERVICES_REQUEST -> showView(binding)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {

        when (requestCode) {
            REQUEST_PERMISSION_LOCATION -> {
                // granted permission
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addMyLocation()
                }
            }
        }
    }

    private fun checkMyLocationLayer() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(binding.rootLayout, "Dibutuhkan akses lokasi untuk mendata barang",
                        Snackbar.LENGTH_SHORT).setAction("OK") { _ ->
                    // meminta permission
                    ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_PERMISSION_LOCATION)
                }.show()

            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSION_LOCATION)
            }

        } else {
            addMyLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun addMyLocation() {
        // tambahkan titik sekarang
        map?.isMyLocationEnabled = true

        // get lokasi sekarang
        fusedLocationClient.lastLocation.addOnSuccessListener(this, this)
    }

    override fun onSuccess(location: Location?) {
        if (location == null) {
            Timber.w("tidak dapat lokasi padahal setMyLocationEnabled true")
            return
        }

        currentLocation = location
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,
                location.longitude), 15f))

        // draw marker
        setupMarker()

        // set listener klik pada marker dan map
        map?.setOnMapClickListener(this)
        map?.setOnMarkerClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            mainViewModel.pasar.set(null)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val selectedPasar = marker.tag as Pasar?
        val location = currentLocation

        // lokasi sekarang dibutuhkan untuk menghitung jarak
        if (location != null && selectedPasar != null) {
            mainViewModel.openSheet(location, selectedPasar)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else
            checkMyLocationLayer()

        return false
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        } else
            super.onBackPressed()
    }

    private fun toPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

    }

    companion object {
        private const val REQUEST_PERMISSION_LOCATION = 1
        private const val FIX_PLAY_SERVICES_REQUEST = 2
        private const val PLACE_PICKER_REQUEST = 1
    }
}