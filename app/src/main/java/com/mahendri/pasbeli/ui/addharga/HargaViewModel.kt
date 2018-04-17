package com.mahendri.pasbeli.ui.addharga

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import android.text.TextUtils
import android.widget.AdapterView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.mahendri.pasbeli.entity.KualitasUnit
import com.mahendri.pasbeli.entity.Resource
import com.mahendri.pasbeli.repository.HargaRepository
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Mahendri
 */

class HargaViewModel @Inject internal constructor(
        private val repository: HargaRepository,
        private val locationClient: FusedLocationProviderClient
) : ViewModel() {

    var barang = repository.allBarang
    val kualitasList = MutableLiveData<List<KualitasUnit>>()
    val nama = MutableLiveData<String>()
    val kualitas = MutableLiveData<String>()
    val unit = MutableLiveData<String>()
    val namaTempat = MutableLiveData<String>()
    val harga = MutableLiveData<String>()
    val location = MutableLiveData<Location>()

    /**
     * data to observe in view
     */
    internal val errorNotif = MutableLiveData<Int>()
    internal val insertStatus = MutableLiveData<Resource<String>>()

    private lateinit var locationRequest: LocationRequest
    private var locationCallback: LocationCallback? = null
    private var kualitasSubject: PublishSubject<String>? = null

    init {
        unit.postValue("satuan")
    }

    /**
     * used by databinding in autocomplete callback
     */
    fun changeKualitas(nama: CharSequence) {
        val namaBarang = nama.toString()
        Timber.d("nama barang berganti: %s", namaBarang)

        if (kualitasSubject == null) {
            kualitasSubject = PublishSubject.create()

            kualitasSubject?.also{
                it.debounce(500, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.computation())
                        .subscribe {
                            Timber.d("ketikan: %s", it)
                            kualitasList.postValue(repository.getKualitas(it))
                        }
            }
        }

        kualitasSubject?.onNext(namaBarang)
    }

    fun changeSelection(adapter: AdapterView<*>?) {
        val selected: KualitasUnit? = adapter?.selectedItem as KualitasUnit?
        if (selected == null) {
            kualitas.postValue(null)
            unit.postValue("satuan")
        } else {
            kualitas.postValue(selected.kualitas)
            unit.postValue("per ${selected.satuan}")
        }
    }

    /**
     * used by databinding in fab click
     */
    internal fun onFabClick(mail: String) {
        val location = this.location.value
        val namaBarang = nama.value
        val kualitasBarang = kualitas.value
        val currentHarga = harga.value
        val currentNamaTempat = namaTempat.value
        if (location == null || location.latitude == 0.0 || location.longitude == 0.0)
            errorNotif.postValue(AddHargaActivity.ERROR_LOCATION)
        else if (TextUtils.isEmpty(namaBarang))
            errorNotif.postValue(AddHargaActivity.ERROR_NAMA)
        else if (TextUtils.isEmpty(kualitasBarang))
            errorNotif.postValue(AddHargaActivity.ERROR_KUALITAS)
        else if (TextUtils.isEmpty(currentHarga))
            errorNotif.postValue(AddHargaActivity.ERROR_HARGA)
        else if (currentHarga != null) {
            Completable
                    .fromAction {
                        val id = repository.getIdBarang(namaBarang, kualitasBarang)
                        if (id == 0) throw IllegalArgumentException("barang tidak valid")

                        repository.insertNewEntry(mail, id, currentHarga.toLong(), currentNamaTempat,
                                location.latitude, location.longitude)
                    }
                    .subscribeOn(Schedulers.computation())
                    .doOnSubscribe {
                        insertStatus.postValue(Resource.loading(null))
                    }
                    .subscribe({
                        insertStatus.postValue(Resource.success("berhasil menambahkan"))
                    }, {
                        insertStatus.postValue(Resource.error(it.message, null))
                    })
        }
    }

    @SuppressLint("MissingPermission")
    internal fun startReqLocation(isAllowed: Boolean) {
        if (isAllowed) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    p0?.let {
                        location.postValue(it.lastLocation)
                    }
                }
            }

            locationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        } else {
            locationCallback?.apply {
                locationClient.removeLocationUpdates(this)
            }
        }
    }

    internal fun setTempat(tempat: String) {
        namaTempat.postValue(tempat)
    }

    internal fun setLocationReq(locRequest: LocationRequest) {
        locationRequest = locRequest
    }
}