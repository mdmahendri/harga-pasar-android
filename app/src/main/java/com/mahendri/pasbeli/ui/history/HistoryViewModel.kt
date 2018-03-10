package com.mahendri.pasbeli.ui.history

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.mahendri.pasbeli.entity.BarangHarga
import com.mahendri.pasbeli.repository.HargaRepository
import javax.inject.Inject

/**
 * @author Mahendri
 */
class HistoryViewModel @Inject constructor (private val repository: HargaRepository) : ViewModel() {

    fun getListHarga(): LiveData<List<BarangHarga>> = repository.catatan

}