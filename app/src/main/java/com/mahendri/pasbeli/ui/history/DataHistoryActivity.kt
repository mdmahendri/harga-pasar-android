package com.mahendri.pasbeli.ui.history

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.mahendri.pasbeli.R

import javax.inject.Inject

import dagger.android.AndroidInjection

class DataHistoryActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var hargaViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history_data)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = HistoryAdapter(null)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        hargaViewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel::class.java)
        hargaViewModel.getListHarga().observe(this, Observer { adapter.swapData(it) })
    }
}
