package com.mahendri.pasbeli.ui.addharga

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.entity.FetchStatus
import com.mahendri.pasbeli.util.AutoValidator
import com.mahendri.pasbeli.util.TextChangeWatch

import java.util.Locale

import javax.inject.Inject

import dagger.android.AndroidInjection

class AddHargaActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var locationCallback: LocationCallback? = null

    private lateinit var namaText: AutoCompleteTextView
    private lateinit var kualitasSpinner: Spinner
    private lateinit var hargaText: TextInputEditText
    private lateinit var namaTempatText: TextInputEditText
    private lateinit var locationText: TextInputEditText
    private lateinit var permissionDialog: AlertDialog

    private var longitude: Double = 0.toDouble()
    private var latitude: Double = 0.toDouble()

    @Inject private lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var hargaViewModel: HargaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val namaTempat = intent.getStringExtra(EXTRA_NAMA_PASAR)
        if (namaTempat == null) finish()

        setContentView(R.layout.activity_add_harga)
        namaText = findViewById(R.id.text_nama_barang)
        kualitasSpinner = findViewById(R.id.spinner_kualitas)
        hargaText = findViewById(R.id.text_harga)
        namaTempatText = findViewById(R.id.text_nama_tempat)
        locationText = findViewById(R.id.text_lat_lng)
        permissionDialog = AlertDialog.Builder(this)
                .setTitle("Lokasi Dibutuhkan")
                .setMessage("Lokasi diperlukan untuk mendata komoditas")
                .setNeutralButton("Keluar") { dialogInterface, i -> finish() }
                .create()
        val fab = findViewById<FloatingActionButton>(R.id.add_komoditi_fab)

        kualitasSpinner.isEnabled = false
        namaTempatText.setText(namaTempat)
        fab.setOnClickListener(this)

        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateLocation()
        } else {
            // belum mendapat permission
            permissionDialog.show()
        }

        hargaViewModel = ViewModelProviders.of(this, viewModelFactory).get(HargaViewModel::class.java)
        initAutoComplete()
        initSpinner()
    }

    private fun initAutoComplete() {

        namaText.addTextChangedListener(object : TextChangeWatch() {
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                hargaViewModel.changeKualitas(charSequence.toString())
            }
        })

        hargaViewModel.listBarang.observe(this, Observer {
            if (it == null || it.fetchStatus == FetchStatus.LOADING) {
                // empty implementation
            } else if (it.fetchStatus == FetchStatus.ERROR) {
                Toast.makeText(this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show()
                finish()
            } else if (it.data != null && it.data.size != 0) {
                namaText.setAdapter<ArrayAdapter<String>>(ArrayAdapter(this,
                        android.R.layout.simple_dropdown_item_1line, it.data))
                namaText.validator = AutoValidator(it.data)
                namaText.setOnFocusChangeListener { view, focus ->
                    if (!focus && view.id == namaText.id) namaText.performValidation() }
            }
        })
    }

    private fun initSpinner() {
        kualitasSpinner.onItemSelectedListener = this

        hargaViewModel.kualitas.observe(this, Observer{ listString ->
            if (listString != null && listString.size != 0) {
                kualitasSpinner.isEnabled = true
                kualitasSpinner.adapter = ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, listString)
            } else
                kualitasSpinner.adapter = null
        })
    }

    override fun onBackPressed() {
        if (permissionDialog.isShowing) {
            permissionDialog.dismiss()
            finish()
        } else
            super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }

    }

    override fun onResume() {
        super.onResume()
        updateLocation()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.add_komoditi_fab -> if (longitude == 0.0 && latitude == 0.0)
                Toast.makeText(this, "lokasi kosong", Toast.LENGTH_SHORT).show()
            else {
                insertDataBaru()
                finish()
            }
        }
    }

    private fun insertDataBaru() {
        if (TextUtils.isEmpty(namaText.text) || TextUtils.isEmpty(hargaText.text)
                || TextUtils.isEmpty(locationText.text)) {
            Toast.makeText(this, "Tidak boleh ada isian kosong", Toast.LENGTH_SHORT).show()
            return
        }

        hargaViewModel.insertNewHarga(java.lang.Long.parseLong(hargaText.text.toString()),
                namaTempatText.text.toString(), latitude, longitude)
    }

    private fun updateLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 5000 // update setiap lima detik

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val lastLocation = locationResult!!.lastLocation
                    longitude = lastLocation.longitude
                    latitude = lastLocation.latitude

                    locationText.setText(String.format(Locale.getDefault(), "%s, %s",
                            latitude, longitude))
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, l: Long) {
        val selected = adapterView.getItemAtPosition(position).toString()
        hargaViewModel.selectBarang(selected)
    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {

    }

    companion object {
        val EXTRA_NAMA_PASAR = "NAMA_PASAR"
    }
}
