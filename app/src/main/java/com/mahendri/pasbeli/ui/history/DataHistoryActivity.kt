package com.mahendri.pasbeli.ui.history

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.databinding.ActivityHistoryDataBinding

import javax.inject.Inject

import dagger.android.AndroidInjection

class DataHistoryActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var hargaViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityHistoryDataBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_history_data)
        hargaViewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel::class.java)
        binding.viewmodel = hargaViewModel

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = HistoryAdapter(null)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        hargaViewModel.getListHarga().observe(this, Observer { adapter.swapData(it) })

        subscrribeView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_history_data, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send_data -> {
                hargaViewModel.sendDataHarga()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun subscrribeView() {
        hargaViewModel.sendHarga.observe(this, Observer {
            if (it != null) {
                hargaViewModel.sendHarga.postValue(null)
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
