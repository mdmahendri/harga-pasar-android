package com.mahendri.pasbeli.repository

import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.mahendri.pasbeli.api.ApiResponse
import com.mahendri.pasbeli.entity.Resource
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * @author Mahendri
 */

abstract class NetworkBoundRes<LocalType, RemoteType> {

    private val result: Flowable<Resource<LocalType>>

    init {

        result = Flowable.defer { loadFromDb() }
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .switchMap { data ->
                    if (shouldFetch(data)) return@switchMap fetchFromNetwork(data)
                    else return@switchMap Flowable.just(Resource.success(data))
                }
                .startWith { Resource.loading(it) }

    }

    private fun fetchFromNetwork(data: LocalType?): Flowable<Resource<LocalType>> {
        return loadFromDb().map { Resource.success(it) }
    }

    public fun asFlowable(): Flowable<Resource<LocalType>> {
        return result;
    }

    protected fun onFetchFailed() {}

    @WorkerThread
    private fun processResponse (response: ApiResponse<RemoteType> ): RemoteType? {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RemoteType)

    @MainThread
    protected abstract fun shouldFetch(data: LocalType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flowable<LocalType>

    @MainThread
    protected abstract fun createCall(): Single<ApiResponse<RemoteType>>
}
