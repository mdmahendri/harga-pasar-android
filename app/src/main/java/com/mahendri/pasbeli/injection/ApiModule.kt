package com.mahendri.pasbeli.injection

import android.app.Application
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mahendri.pasbeli.BuildConfig
import com.mahendri.pasbeli.api.WebService
import com.mahendri.pasbeli.preference.Constants
import com.mahendri.pasbeli.viewmodel.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Mahendri
 */

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideWebService(): WebService {
        // get default okhttp client
        val httpClient = OkHttpClient.Builder()

        // add logging as last interceptor
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        // gson naming policy
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        return Retrofit.Builder()
                .baseUrl(Constants.WEB_SERVICE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()
                .create(WebService::class.java)
    }


    @Singleton
    @Provides
    fun locationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Singleton
    @Provides
    fun providePlayDriver(app: Application): GooglePlayDriver {
        return GooglePlayDriver(app)
    }

    @Singleton
    @Provides
    fun provideDispatcher(googlePlayDriver: GooglePlayDriver): FirebaseJobDispatcher {
        return FirebaseJobDispatcher(googlePlayDriver)
    }

    @Singleton
    @Provides
    fun provideSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
    }

    @Singleton
    @Provides
    fun provideSignInClient(app: Application, options: GoogleSignInOptions)
            : GoogleSignInClient {
        return GoogleSignIn.getClient(app, options)
    }
}