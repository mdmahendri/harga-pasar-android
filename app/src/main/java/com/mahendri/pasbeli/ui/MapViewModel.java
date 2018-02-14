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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Mahendri
 */

public class MapViewModel extends ViewModel {

    private MapRepository mapRepository;
    private HargaRepository hargaRepository;

    private MutableLiveData<Resource<String>> uploadResponse = new MutableLiveData<>();

    @Inject
    MapViewModel(MapRepository mapRepository, HargaRepository hargaRepository) {
        this.mapRepository = mapRepository;
        this.hargaRepository = hargaRepository;
    }

    LiveData<Resource<List<Pasar>>> getMapNearby(Location currentLoc) {
        return mapRepository.getNearbyPasar(currentLoc);
    }

    LiveData<Resource<String>> getUploadStatus() {
        return uploadResponse;
    }

    void sendDataHarga() {
        uploadResponse.setValue(Resource.loading(null));
        hargaRepository.sendDataEntry().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null)
                    uploadResponse.setValue(Resource.success(response.body()));
                else
                    uploadResponse.setValue(Resource.error("error HTTP response", null));
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                uploadResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }
}
