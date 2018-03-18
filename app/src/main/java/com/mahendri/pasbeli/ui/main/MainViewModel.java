package com.mahendri.pasbeli.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.repository.HargaRepository;
import com.mahendri.pasbeli.repository.MapRepository;
import com.mahendri.pasbeli.util.DistanceConvert;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Mahendri
 */

public class MainViewModel extends ViewModel {

    public final ObservableField<Pasar> pasar = new ObservableField<>();
    public final ObservableField<String> distance = new ObservableField<>();

    final MutableLiveData<String> addPasar = new MutableLiveData<>();
    final MutableLiveData<String> sendHarga = new MutableLiveData<>();
    final MutableLiveData<String> intentAdd = new MutableLiveData<>();

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MapRepository mapRepository;
    private final HargaRepository hargaRepository;

    @Inject
    MainViewModel(MapRepository mapRepository, HargaRepository hargaRepository) {
        this.mapRepository = mapRepository;
        this.hargaRepository = hargaRepository;
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
    }

    LiveData<Resource<List<Pasar>>> getMapNearby(Location currentLoc) {
        return mapRepository.getNearbyPasar(currentLoc);
    }

    void sendDataHarga() {
        disposable.add(hargaRepository.sendDataEntry().subscribe(
                () -> sendHarga.postValue("Sukses mengirim data"),
                throwable -> sendHarga.postValue(throwable.getMessage())
        ));
    }

    void openSheet(Location currentLocation, Pasar select) {
        pasar.set(select);
        distance.set(String.format("%s KM", DistanceConvert.toKm(currentLocation, select.getLocation())));
    }

    void addPasar(Place place) {
        disposable.add(
                mapRepository.saveNewPasar(place)
                .subscribe(
                        () -> addPasar.postValue("Berhasil menambahkan pasar."),
                        throwable -> addPasar.postValue(throwable.getMessage())
                )
        );
    }

    /**
     * Called by the Data Binding library, bottom sheet click listener.
     */
    public void onAddClick() {
        intentAdd.postValue(pasar.get().nama);
    }

}
