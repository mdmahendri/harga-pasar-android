package com.mahendri.pasbeli.ui.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mahendri.pasbeli.databinding.ItemKomoditiBinding
import com.mahendri.pasbeli.entity.BarangHarga

/**
 * @author Mahendri Dwicahyo
 */

class HistoryAdapter internal constructor(
        private var data: List<BarangHarga>?
) : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemKomoditiBinding.inflate(inflater, parent, false)
        return HistoryHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        data?.let {
            val barang = it[position]
            holder.bind(barang)
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    internal fun swapData(newData: List<BarangHarga>?) {
        if (newData != null) {
            data = newData
            notifyDataSetChanged()
        }
    }

    inner class HistoryHolder(private val binding: ItemKomoditiBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(barangHarga: BarangHarga) {
            binding.catatan = barangHarga
            binding.executePendingBindings()
        }

    }
}