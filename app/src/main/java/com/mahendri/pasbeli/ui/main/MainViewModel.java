package com.mahendri.pasbeli.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.location.Location;

import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.repository.HargaRepository;
import com.mahendri.pasbeli.repository.MapRepository;
import com.mahendri.pasbeli.util.DistanceConvert;
import com.mahendri.pasbeli.viewmodel.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * @author Mahendri
 */

public class MainViewModel extends ViewModel {

    public final ObservableField<Pasar> pasar = new ObservableField<>();
    public final ObservableField<String> distance = new ObservableField<>();

    private final MapRepository mapRepository;
    private final HargaRepository hargaRepository;

    private final SingleLiveEvent<String> addNewEvent = new SingleLiveEvent<>();

    @Inject
    MainViewModel(MapRepository mapRepository, HargaRepository hargaRepository) {
        this.mapRepository = mapRepository;
        this.hargaRepository = hargaRepository;
    }

    LiveData<Resource<List<Pasar>>> getMapNearby(Location currentLoc) {
        return mapRepository.getNearbyPasar(currentLoc);
    }

    Completable sendDataHarga() {
        return hargaRepository.sendDataEntry();
    }

    void openSheet(Location currentLocation, Pasar select) {
        pasar.set(select);
        distance.set(String.format("%s KM", DistanceConvert.toKm(currentLocation, select.getLocation())));
    }

    SingleLiveEvent<String> getAddNewEvent() {
        return addNewEvent;
    }

    /**
     * Called by the Data Binding library, bottom sheet click listener.
     */
    public void onAddClick(Pasar pasar) {
        addNewEvent.setValue(pasar.nama);
    }
}
