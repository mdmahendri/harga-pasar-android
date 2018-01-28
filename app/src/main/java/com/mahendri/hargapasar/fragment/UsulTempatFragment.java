package com.mahendri.hargapasar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahendri.hargapasar.unused.AddUsulanActivity;
import com.mahendri.hargapasar.R;
import com.mahendri.hargapasar.unused.UsulTempat;
import com.mahendri.hargapasar.unused.UsulTempatAdapter;

import java.util.ArrayList;

/**
 * @author Mahendri Dwicahyo
 */

public class UsulTempatFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usul_tempat, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<UsulTempat> data = generateData();
        UsulTempatAdapter adapter = new UsulTempatAdapter(data);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = v.findViewById(R.id.add_usul_tempat);
        fab.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), AddUsulanActivity.class));
    }

    private ArrayList<UsulTempat> generateData() {
        ArrayList<UsulTempat> data = new ArrayList<>();
        data.add(new UsulTempat(500, R.drawable.toko1,"Pasar Serbaguna", "Jalan 123, Kebon Jeruk, Jakarta Barat"));
        data.add(new UsulTempat(-10, R.drawable.toko2,"Pasar Belakang Kos", "Jalan WR Supratman, Sragen, Jawa Tengah"));
        return data;
    }
}
