package com.mahendri.pasbeli.ui.addharga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.FetchStatus;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.repository.HargaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Mahendri
 */

public class HargaViewModel extends ViewModel {

    private int idBarang;
    private String namaBarang;

    private PublishSubject<String> kualitasSubject;
    private PublishSubject<String> barangSubject;

    private HargaRepository repository;
    private MutableLiveData<List<String>> kualitas = new MutableLiveData<>();

    @Inject
    HargaViewModel(HargaRepository repository) {
        this.repository = repository;
    }

    LiveData<Resource<List<String>>> getListBarang() {;
        return Transformations.map(repository.getAllBarang(), resourceList -> {
            ArrayList<String> namaList = new ArrayList<>();
            if (resourceList.data != null) {
                for (Barang barang : resourceList.data)
                    if (!namaList.contains(barang.getNama()))namaList.add(barang.getNama());

                return Resource.success(namaList);
            } else if (resourceList.fetchStatus == FetchStatus.LOADING)
                return Resource.loading(null);
            else
                return Resource.error("gagal ambil", null);
        });
    }

    void changeKualitas(String namaBarang) {
        if (kualitasSubject == null) {
            kualitasSubject = PublishSubject.create();
            kualitasSubject.debounce(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe(stringNama -> {
                        this.namaBarang = stringNama;
                        List<String> kualitasList = repository.getKualitas(stringNama);
                        kualitas.postValue(kualitasList);
                    });
        }

        kualitasSubject.onNext(namaBarang);
    }

    LiveData<List<String>> getKualitas() {
        return kualitas;
    }

    void insertNewHarga(long harga, String namaTempat, double latitude, double longitude) {
        repository.insertNewEntry(idBarang, harga, namaTempat, latitude, longitude);
    }

    void selectBarang(String kualitas) {
        if (barangSubject == null) {
            barangSubject = PublishSubject.create();
            barangSubject.debounce(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe(selected -> this.idBarang = repository.getIdBarang(namaBarang, selected));
        }

        barangSubject.onNext(kualitas);
    }
}
