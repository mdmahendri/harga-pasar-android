package com.mahendri.pasbeli.ui.addharga

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.location.LocationRequest
import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.databinding.ActivityAddHargaBinding
import com.mahendri.pasbeli.entity.FetchStatus
import dagger.android.AndroidInjection
import javax.inject.Inject

class AddHargaActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var locationRequest: LocationRequest
    @Inject lateinit var permissionDialog: AlertDialog
    private lateinit var hargaViewModel: HargaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val namaTempat = intent.getStringExtra(EXTRA_NAMA_PASAR)
        if (namaTempat == null) finish()

        val binding: ActivityAddHargaBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_add_harga)
        binding.setLifecycleOwner(this)

        hargaViewModel = ViewModelProviders.of(this, viewModelFactory).get(HargaViewModel::class.java)
        binding.vm = hargaViewModel
        hargaViewModel.setTempat(namaTempat)
        hargaViewModel.setLocationReq(locationRequest)

        subscribeEvent(binding, hargaViewModel)
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
        requestLocation(false)

    }

    override fun onResume() {
        super.onResume()
        requestLocation(true)
    }

    private fun requestLocation(status: Boolean) {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            hargaViewModel.startReqLocation(status)
        } else {
            // belum mendapat permission
            permissionDialog.show()
        }
    }

    private fun subscribeEvent(binding: ActivityAddHargaBinding, hargaViewModel: HargaViewModel) {
        hargaViewModel.run {
            errorNotif.observe(this@AddHargaActivity, Observer {
                if (it == NORMAL) return@Observer

                errorNotif.postValue(NORMAL)
                val status: String = when (it) {
                    ERROR_LOCATION ->  "Kesalahan dalam mengambil lokasi"
                    ERROR_HARGA -> "Pastikan harga sudah terisi"
                    ERROR_NAMA -> "Nama barang kosong"
                    ERROR_KUALITAS -> "Pilih jenis barang"
                    else -> ""
                }
                if (status.length > 0)
                    Toast.makeText(this@AddHargaActivity, status, Toast.LENGTH_SHORT).show()
            })

            insertStatus.observe(this@AddHargaActivity, Observer {
                val fab = binding.addKomoditiFab
                when (it?.fetchStatus) {
                    FetchStatus.LOADING -> fab.isEnabled = false
                    FetchStatus.SUCCESS -> {
                        Toast.makeText(this@AddHargaActivity, it.data, Toast.LENGTH_SHORT)
                                .show()
                        fab.isEnabled = true
                        finish()
                    }
                    FetchStatus.ERROR -> {
                        Toast.makeText(this@AddHargaActivity, it.data, Toast.LENGTH_SHORT)
                                .show()
                        fab.isEnabled = true
                    }
                }
            })
        }

    }

    @Suppress("UNUSED_PARAMETER") //view required by xml
    fun onFabClick(view: View) {
        val account = GoogleSignIn.getLastSignedInAccount(this)?.email
        if (account != null) {
            hargaViewModel.onFabClick(account)
        } else {
            Toast.makeText(this, "Ulangi login dari awal", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    companion object {
        const val EXTRA_NAMA_PASAR = "NAMA_PASAR"
        const val NORMAL = 0
        const val ERROR_LOCATION = 1
        const val ERROR_NAMA = 2
        const val ERROR_KUALITAS = 3
        const val ERROR_HARGA = 4
    }
}