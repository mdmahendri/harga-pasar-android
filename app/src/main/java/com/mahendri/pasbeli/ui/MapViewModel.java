package com.mahendri.pasbeli.ui;

import android.arch.lifecycle.ViewModel;
import android.location.Location;

import com.mahendri.pasbeli.api.map.PlaceResult;
import com.mahendri.pasbeli.repository.MapRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Mahendri
 */

class MapViewModel extends ViewModel {

    private MapRepository mapRepository;

    @Inject
    MapViewModel(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    List<PlaceResult> getMapNearby(Location currentLoc) {
        return mapRepository.getNearbyPasar(currentLoc);
    }
}
