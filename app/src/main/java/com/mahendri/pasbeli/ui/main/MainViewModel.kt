package com.mahendri.pasbeli.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.location.Location
import com.google.android.gms.location.places.Place
import com.mahendri.pasbeli.entity.Pasar
import com.mahendri.pasbeli.entity.Resource
import com.mahendri.pasbeli.repository.MapRepository
import com.mahendri.pasbeli.util.AppUtil
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author Mahendri
 */

class MainViewModel @Inject constructor(
        private val mapRepository: MapRepository
) : ViewModel() {

    val pasar = ObservableField<Pasar>()
    val distance = ObservableField<String>()

    internal val addPasar = MutableLiveData<String>()
    internal val intentAdd = MutableLiveData<String>()

    private val disposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
    }

    internal fun getMapNearby(currentLoc: Location): LiveData<Resource<List<Pasar>>> {
        return mapRepository.getNearbyPasar(currentLoc)
    }

    internal fun openSheet(currentLocation: Location, select: Pasar) {
        pasar.set(select)
        distance.set(String.format("%s KM", AppUtil.toKm(currentLocation, select.location)))
    }

    internal fun addPasar(place: Place) {
        disposable.add(
                mapRepository.saveNewPasar(place)
                        .subscribe({ addPasar.postValue("Berhasil menambahkan pasar.")})
                        { throwable -> addPasar.postValue(throwable.message) }
        )
    }

    /**
     * Called by the Data Binding library, bottom sheet click listener.
     */
    fun onAddClick() {
        intentAdd.postValue(pasar.get()?.nama)
    }

}
