package com.mahendri.pasbeli.ui.addharga

import android.support.v7.app.AlertDialog
import com.google.android.gms.location.LocationRequest
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit

@Module
class AddHargaModule {

    @Provides
    internal fun provideLocationRequest() : LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = TimeUnit.SECONDS.toMillis(5)

        return locationRequest
    }

    @Provides
    internal fun provideDialog(activity: AddHargaActivity): AlertDialog {
        return AlertDialog.Builder(activity)
                .setTitle("Lokasi Dibutuhkan")
                .setMessage("Lokasi diperlukan untuk mendata komoditas")
                .setNeutralButton("Keluar") { _, _ -> activity.finish() }
                .create()
    }
}