package com.mahendri.pasbeli.ui.harga;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mahendri.pasbeli.R;
import com.mahendri.pasbeli.entity.data.HargaKomoditas;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DataHistoryActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    HargaViewModel hargaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_komoditi_list);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final HistoryAdapter adapter = new HistoryAdapter(null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        hargaViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(HargaViewModel.class);
        hargaViewModel.getListHargaKomoditas().observe(this,
                new Observer<List<HargaKomoditas>>() {
                    @Override
                    public void onChanged(@Nullable List<HargaKomoditas> hargaKomoditas) {
                        adapter.swapData(hargaKomoditas);
                    }
                });
    }
}
