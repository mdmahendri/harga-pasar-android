package com.mahendri.pasbeli.ui.harga;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahendri.pasbeli.R;
import com.mahendri.pasbeli.entity.HargaKomoditas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Mahendri Dwicahyo
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.KomoditiHolder> {

    private List<HargaKomoditas> data;

    HistoryAdapter(List<HargaKomoditas> data) {
        this.data = data;
    }

    @Override
    public KomoditiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_komoditi, parent, false);
        return new KomoditiHolder(v);
    }

    @Override
    public void onBindViewHolder(KomoditiHolder holder, int position) {
        HargaKomoditas komoditi = data.get(position);
        holder.setData(komoditi);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    void swapData(List<HargaKomoditas> newData) {
        if (newData != null) {
            data = newData;
            notifyDataSetChanged();
        }
    }

    class KomoditiHolder extends RecyclerView.ViewHolder {

        private TextView dataText;
        private TextView locationText;
        private TextView timeText;

        KomoditiHolder(View itemView) {
            super(itemView);
            dataText = itemView.findViewById(R.id.komoditi_data);
            locationText = itemView.findViewById(R.id.komoditi_tempat);
            timeText = itemView.findViewById(R.id.komoditi_waktu);
        }

        void setData (HargaKomoditas hargaKomoditas) {
            dataText.setText(String.format("mendata %s seharga Rp %s",
                    hargaKomoditas.namaKomoditas, hargaKomoditas.hargaKomoditas));
            locationText.setText(hargaKomoditas.namaTempat);
            timeText.setText(convertStringDate(hargaKomoditas.waktuCatat));
        }

        String convertStringDate(long time){
            Calendar c = Calendar.getInstance();

            // set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            long today = c.getTimeInMillis();

            if(time > today){
                SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return formatDate.format(new Date(time));
            } else {
                SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM", Locale.getDefault());
                return formatDate.format(new Date(time));
            }
        }

    }
}
