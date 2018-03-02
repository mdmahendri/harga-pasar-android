package com.mahendri.pasbeli.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;
import android.support.annotation.NonNull;

import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.repository.HargaRepository;
import com.mahendri.pasbeli.repository.MapRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Mahendri
 */

public class MapViewModel extends ViewModel {

    private final MapRepository mapRepository;
    private final HargaRepository hargaRepository;

    @Inject
    MapViewModel(MapRepository mapRepository, HargaRepository hargaRepository) {
        this.mapRepository = mapRepository;
        this.hargaRepository = hargaRepository;
    }

    LiveData<Resource<List<Pasar>>> getMapNearby(Location currentLoc) {
        return mapRepository.getNearbyPasar(currentLoc);
    }

    Completable sendDataHarga() {
        return hargaRepository.sendDataEntry();
    }
}
