package com.mahendri.pasbeli.ui.history;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mahendri.pasbeli.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DataHistoryActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    HistoryViewModel hargaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_data);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final HistoryAdapter adapter = new HistoryAdapter(null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        hargaViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(HistoryViewModel.class);
        hargaViewModel.getListHarga().observe(this, adapter::swapData);
    }
}
