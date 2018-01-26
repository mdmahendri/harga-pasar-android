package com.mahendri.hargapasar;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class KomoditiListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komoditi_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Komoditi> data = generateData();
        KomoditiAdapter adapter = new KomoditiAdapter(data);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.komoditi_fab_btn);
        fab.setOnClickListener(this);
    }

    private ArrayList<Komoditi> generateData() {
        ArrayList<Komoditi> data = new ArrayList<>();
        data.add(new Komoditi(0, 10, 10000, 15000, "Bayam", "kilogram"));
        data.add(new Komoditi(0, 10, 10000, 15000, "Terong", "kilogram"));
        data.add(new Komoditi(0, 10, 10000, 15000, "Wortel", "kilogram"));
        data.add(new Komoditi(0, 10, 10000, 15000, "Kelapa", "kilogram"));
        return data;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.komoditi_fab_btn:
                String namaTempat = getIntent().getStringExtra("LOC_NAME");
                Intent toAddKomoditi = new Intent(this, AddKomoditiActivity.class);
                toAddKomoditi.putExtra("LOC_NAME", namaTempat);
                startActivity(toAddKomoditi);
                break;
        }
    }
}
