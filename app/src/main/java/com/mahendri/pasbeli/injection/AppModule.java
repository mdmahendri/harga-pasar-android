package com.mahendri.pasbeli.injection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mahendri.pasbeli.BuildConfig;
import com.mahendri.pasbeli.api.GoogleMapService;
import com.mahendri.pasbeli.api.WebService;
import com.mahendri.pasbeli.database.BarangDao;
import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.database.PasBeliDb;
import com.mahendri.pasbeli.database.PasarDao;
import com.mahendri.pasbeli.preference.Constants;
import com.mahendri.pasbeli.viewmodel.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GoogleMapService.class);
    }

    @Singleton @Provides
    WebService provideWebService() {
        // get default okhttp client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // add logging as last interceptor
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        // gson naming policy
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return new Retrofit.Builder()
                .baseUrl(Constants.WEB_SERVICE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()
                .create(WebService.class);
    }

    @Singleton @Provides
    PasBeliDb provideDatabase(Application app) {
        return Room.databaseBuilder(app, PasBeliDb.class, "pasbeli.db").build();
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

    @Singleton @Provides
    FusedLocationProviderClient locationProviderClient(Application app) {
        return LocationServices.getFusedLocationProviderClient(app);
    }
}
