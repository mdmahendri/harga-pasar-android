package com.mahendri.hargapasar.injection;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.mahendri.hargapasar.database.HargaKomoditasDao;
import com.mahendri.hargapasar.database.HargaKomoditasRepository;
import com.mahendri.hargapasar.database.HargaPasarDatabase;
import com.mahendri.hargapasar.viewmodel.CustomViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Mahendri
 */

@Module
public class RoomModule {

    private final HargaPasarDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                HargaPasarDatabase.class,
                "HargaPasar.db"
        ).build();
    }

    @Singleton @Provides
    HargaKomoditasRepository provideHargaRepository(HargaKomoditasDao hargaKomoditasDao) {
        return new HargaKomoditasRepository(hargaKomoditasDao);
    }

    @Singleton @Provides
    HargaKomoditasDao provideHargaKomoditasDao(HargaPasarDatabase hargaPasarDatabase) {
        return hargaPasarDatabase.hargaKomoditasDao();
    }

    @Singleton @Provides
    HargaPasarDatabase provideHargaPasarDatabase() {
        return database;
    }

    @Singleton @Provides
    ViewModelProvider.Factory provideViewModelFactory(HargaKomoditasRepository repository) {
        return new CustomViewModelFactory(repository);
    }

}
