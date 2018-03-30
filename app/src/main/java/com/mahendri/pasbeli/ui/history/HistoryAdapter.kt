package com.mahendri.pasbeli.ui.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.entity.BarangHarga
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Mahendri Dwicahyo
 */

class HistoryAdapter internal constructor(
        private var data: List<BarangHarga>?
) : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.item_komoditi, parent, false)
        return HistoryHolder(v)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        data?.let {
            val barang = it[position]
            holder.setData(barang)
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

    inner class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dataText: TextView
        private val locationText: TextView
        private val timeText: TextView

        init {
            dataText = itemView.findViewById(R.id.komoditi_data)
            locationText = itemView.findViewById(R.id.komoditi_tempat)
            timeText = itemView.findViewById(R.id.komoditi_waktu)
        }

        fun setData(barangHarga: BarangHarga) {
            dataText.text = String.format("mendata %s seharga Rp %s", barangHarga.nama, barangHarga.harga)
            locationText.text = barangHarga.namaTempat
            timeText.text = convertStringDate(barangHarga.waktuCatat)
        }

        fun convertStringDate(time: Long): String {
            val c = Calendar.getInstance()

            // set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            val today = c.timeInMillis

            if (time > today) {
                val formatDate = SimpleDateFormat("HH:mm", Locale.getDefault())
                return formatDate.format(Date(time))
            } else {
                val formatDate = SimpleDateFormat("dd MMM", Locale.getDefault())
                return formatDate.format(Date(time))
            }
        }

    }
}