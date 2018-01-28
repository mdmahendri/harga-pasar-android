package com.mahendri.hargapasar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahendri.hargapasar.KomoditiListActivity;
import com.mahendri.hargapasar.R;
import com.mahendri.hargapasar.entity.TempatPasar;
import com.mahendri.hargapasar.TempatPasarAdapter;

import java.util.ArrayList;

/**
 * @author Mahendri
 */

public class HomeFragment extends Fragment implements TempatPasarAdapter.TempatClickListener {

    private ArrayList<TempatPasar> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        data = generateData();
        TempatPasarAdapter adapter = new TempatPasarAdapter(data, this);
        recyclerView.setAdapter(adapter);

        return v;
    }

    private ArrayList<TempatPasar> generateData() {
        ArrayList<TempatPasar> data = new ArrayList<>();
        data.add(new TempatPasar(R.drawable.toko1,"Pasar Serbaguna", 12, 500));
        data.add(new TempatPasar(R.drawable.toko2,"Pasar Belakang Kos", 25, 1200));
        return data;
    }

    @Override
    public void onTempatClick(int position, String nama) {
        Intent intent = new Intent(getActivity(), KomoditiListActivity.class);
        intent.putExtra("LOC_NAME", nama);
        startActivity(intent);
    }
}
