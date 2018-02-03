package com.mahendri.pasbeli.injection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.mahendri.pasbeli.api.GoogleMapService;
import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.database.PasBeliDatabase;
import com.mahendri.pasbeli.database.PasarDao;
import com.mahendri.pasbeli.preference.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Mahendri
 */

@Module(includes = {ViewModelModule.class})
class AppModule {

    @Singleton @Provides
    GoogleMapService provideMapService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.MAP_WEB_SERVICE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GoogleMapService.class);
    }

    @Singleton @Provides
    PasBeliDatabase provideDatabase(Application app) {
        return Room.databaseBuilder(app, PasBeliDatabase.class, "pasbeli.db").build();
    }

    @Singleton @Provides
    HargaDao provideHargaKomoditasDao(PasBeliDatabase pasBeliDatabase) {
        return pasBeliDatabase.hargaKomoditasDao();
    }

    @Singleton @Provides
    PasarDao providePasarDao(PasBeliDatabase pasBeliDatabase) {
        return pasBeliDatabase.pasarDao();
    }
}
