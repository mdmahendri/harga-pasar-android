package com.mahendri.pasbeli.ui.addharga

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

import com.mahendri.pasbeli.entity.Barang
import com.mahendri.pasbeli.entity.FetchStatus
import com.mahendri.pasbeli.entity.Resource
import com.mahendri.pasbeli.repository.HargaRepository

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * @author Mahendri
 */

class HargaViewModel @Inject internal constructor(
        private val repository: HargaRepository
) : ViewModel() {

    private var idBarang: Int = 0
    private var namaBarang: String? = null

    private var kualitasSubject: PublishSubject<String>? = null
    private var barangSubject: PublishSubject<String>? = null
    private val kualitas = MutableLiveData<List<String>>()

    internal val listBarang: LiveData<Resource<out ArrayList<String>>>
        get() = Transformations.map(repository.allBarang) { resourceList ->
            val namaList = ArrayList<String>()
            if (resourceList.data != null) {
                for ((_, nama) in resourceList.data)
                    if (!namaList.contains(nama)) namaList.add(nama)
                return@map Resource.success(namaList)
            } else if (resourceList.fetchStatus == FetchStatus.LOADING)
                return@map Resource.loading(null)
            else
                return@map Resource.error("gagal ambil",null)
        }

    internal fun changeKualitas(namaBarang: String) {
        if (kualitasSubject == null) {
            kualitasSubject = PublishSubject.create()
            kualitasSubject!!.debounce(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe { stringNama ->
                        this.namaBarang = stringNama
                        val kualitasList = repository.getKualitas(stringNama)
                        kualitas.postValue(kualitasList)
                    }
        }

        kualitasSubject?.onNext(namaBarang)
    }

    internal fun getKualitas(): LiveData<List<String>> {
        return kualitas
    }

    internal fun insertNewHarga(harga: Long, namaTempat: String, latitude: Double, longitude: Double) {
        repository.insertNewEntry(idBarang, harga, namaTempat, latitude, longitude)
    }

    internal fun selectBarang(kualitas: String) {
        if (barangSubject == null) {
            barangSubject = PublishSubject.create()
            barangSubject!!.debounce(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe { selected -> this.idBarang = repository.getIdBarang(namaBarang, selected) }
        }

        barangSubject?.onNext(kualitas)
    }
}
