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
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.databinding.ActivityHistoryDataBinding

import javax.inject.Inject

import dagger.android.AndroidInjection

class DataHistoryActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var dataViewModel: HistoryViewModel
    lateinit var countTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityHistoryDataBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_history_data)
        dataViewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel::class.java)
        binding.viewmodel = dataViewModel

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = HistoryAdapter(null)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        dataViewModel.getListHarga().observe(this, Observer { adapter.swapData(it) })

        subscrribeView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_history_data, menu)
        menu?.let {
            val pointsBadge = it.findItem(R.id.points)
            pointsBadge.setActionView(R.layout.badge_point)
            val countTextView = pointsBadge.actionView
                    .findViewById<RelativeLayout>(R.id.container)
                    .findViewById<TextView>(R.id.badge_count)
            dataViewModel.requestCount().observe(this, Observer {
                countTextView.text = String.format("%s poin", it)
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.send_data -> {
                dataViewModel.sendDataHarga()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun subscrribeView() {
        dataViewModel.sendHarga.observe(this, Observer {
            if (it != null) {
                dataViewModel.sendHarga.postValue(null)
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
