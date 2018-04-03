package com.mahendri.pasbeli.util

import android.location.Location

import com.google.android.gms.maps.model.LatLng

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Mahendri
 */

object AppUtil {

    fun toKm(currentLocation: Location, latLng: LatLng): String {

        // konversi LatLng ke Location
        val loc = Location("")
        loc.latitude = latLng.latitude
        loc.longitude = latLng.longitude
        val distance = currentLocation.distanceTo(loc) / 1000

        // pembulatan distance
        val decimalFormat = DecimalFormat("#.#")
        decimalFormat.roundingMode = RoundingMode.CEILING
        return decimalFormat.format(distance.toDouble())
    }

    fun convertStringDate(time: Long): String {
        val c = Calendar.getInstance()

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val today = c.timeInMillis

        if (time > today) {
            val formatDate = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatDate.format(Date(time))
        } else {
            val formatDate = SimpleDateFormat("dd MMM", Locale.getDefault())
            return formatDate.format(Date(time))
        }
    }

}
