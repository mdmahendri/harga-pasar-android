package com.mahendri.pasbeli.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;

import com.mahendri.pasbeli.api.map.PlaceResult;
import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.repository.MapRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Mahendri
 */

public class MapViewModel extends ViewModel {

    private MapRepository mapRepository;

    @Inject
    MapViewModel(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    LiveData<Resource<List<Pasar>>> getMapNearby(Location currentLoc) {
        return mapRepository.getNearbyPasar(currentLoc);
    }
}
