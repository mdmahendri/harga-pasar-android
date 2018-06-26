package com.mahendri.pasbeli.ui.history

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.mahendri.pasbeli.entity.BarangHarga
import com.mahendri.pasbeli.repository.HargaRepository
import com.mahendri.pasbeli.repository.UserRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author Mahendri
 */
class HistoryViewModel @Inject constructor (
    private val repository: HargaRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    val isSend = ObservableBoolean(false)

    internal val sendHarga = MutableLiveData<String>()
    private val pointsText = MutableLiveData<String>()

    private val disposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
    }

    fun getListHarga(): LiveData<List<BarangHarga>> = repository.catatan

    internal fun sendDataHarga() {
        disposable.add(repository.sendDataEntry()
                .doOnSubscribe { isSend.set(true) }
                .doOnTerminate { isSend.set(false) }
                .subscribe({
                    userRepo.updatePoints().subscribe {
                        value -> pointsText.postValue(value.toString())
                    }
                    sendHarga.postValue("Sukses mengirim data")
                }, {
                    throwable -> sendHarga.postValue(throwable.message)
                }))
    }

    internal fun requestCount(): LiveData<String> {
        userRepo.loadPoints()
                .subscribe {
                    value -> pointsText.postValue(value.toString())
                }

        return pointsText
    }
}