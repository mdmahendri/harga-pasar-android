package com.mahendri.pasbeli.repository

import com.mahendri.pasbeli.api.WebService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Mahendri
 */
@Singleton
class UserRepository @Inject constructor(
    private val webService: WebService,
    private val pref: SharedPref
){
    fun loadPoints(): Single<Int> {
        return Single.fromCallable {
            pref.getPoints()
        }.flatMap {
            if (it == -1) webService.fetchPoints(pref.getMail())
            else Single.just(it)
        }.subscribeOn(Schedulers.io())
    }

    fun updatePoints(): Single<Int> {
        return webService.fetchPoints(pref.getMail())
                .subscribeOn(Schedulers.io())
    }

    fun saveMail(mail: String?): Completable {
        return Completable.fromAction {
            if (mail == null) throw Exception("gagal login, coba lagi")
            else pref.saveMail(mail)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun clearPref(): Completable {
        return Completable.fromAction {
            pref.clear()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}