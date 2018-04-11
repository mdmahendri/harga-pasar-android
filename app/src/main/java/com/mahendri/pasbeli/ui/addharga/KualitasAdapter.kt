package com.mahendri.pasbeli.ui.addharga

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.entity.KualitasUnit

/**
 * @author Mahendri
 */

class KualitasAdapter (
        context: Context?,
        resource: Int,
        objects: List<KualitasUnit>?
) : ArrayAdapter<KualitasUnit>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return rowView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return rowView(position, convertView, parent)
    }

    private fun rowView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: AdapterHolder
        val kualitas = getItem(position)
        val row: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            row = inflater.inflate(R.layout.item_spinner, parent, false)
            viewHolder = AdapterHolder(row)
            row.tag = viewHolder
        } else {
            row = convertView
            viewHolder = row.tag as AdapterHolder
        }

        viewHolder.bind(kualitas)
        return row
    }

    inner class AdapterHolder(private val textView: TextView) {

        constructor(view: View) : this(view.findViewById(R.id.text_kualitas))

        fun bind(item: KualitasUnit) {
            textView.text = item.kualitas
        }
    }
}