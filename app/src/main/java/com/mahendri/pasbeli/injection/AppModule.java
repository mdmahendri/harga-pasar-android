package com.mahendri.pasbeli.injection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.mahendri.pasbeli.database.BarangDao;
import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.database.PasBeliDb;
import com.mahendri.pasbeli.database.PasarDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Mahendri
 */

@Module(includes = {ViewModelModule.class, ApiModule.class})
class AppModule {

    @Singleton @Provides
    PasBeliDb provideDatabase(Application app) {
        return Room
        		.databaseBuilder(app, PasBeliDb.class, "pasbeli.db")
        		.addMigrations(PasBeliDb.Companion.getMIGRATION_1_2(), PasBeliDb.Companion.getMIGRATION_2_3(),
                        PasBeliDb.Companion.getMIGRATION_3_4())
        		.build();
    }

    @Singleton @Provides
    HargaDao provideHargaKomoditasDao(PasBeliDb pasBeliDb) {
        return pasBeliDb.hargaKomoditasDao();
    }

    @Singleton @Provides
    PasarDao providePasarDao(PasBeliDb pasBeliDb) {
        return pasBeliDb.pasarDao();
    }

    @Singleton @Provides
    BarangDao provideBarangDao(PasBeliDb pasBeliDb) {
        return pasBeliDb.barangDao();
    }

}
