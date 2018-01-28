package com.mahendri.hargapasar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahendri.hargapasar.entity.Komoditi;

import java.util.ArrayList;

/**
 * @author Mahendri Dwicahyo
 */

public class KomoditiAdapter extends RecyclerView.Adapter<KomoditiAdapter.KomoditiHolder> {

    private ArrayList<Komoditi> data;

    KomoditiAdapter (ArrayList<Komoditi> data) {
        this.data = data;
    }

    @Override
    public KomoditiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_komoditi, null);
        return new KomoditiHolder(v);
    }

    @Override
    public void onBindViewHolder(KomoditiHolder holder, int position) {
        Komoditi komoditi = data.get(position);
        holder.setData(komoditi);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class KomoditiHolder extends RecyclerView.ViewHolder {

        private Context context;
        private ImageView komoditiImg;
        private TextView komoditiName;
        private TextView komodiitiReward;
        private TextView komoditiRange;
        private TextView komoditiSatuan;

        KomoditiHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            komoditiImg = itemView.findViewById(R.id.komoditi_img);
            komoditiName = itemView.findViewById(R.id.komoditi_name);
            komodiitiReward = itemView.findViewById(R.id.komoditi_reward);
            komoditiRange = itemView.findViewById(R.id.komoditi_range);
            komoditiSatuan = itemView.findViewById(R.id.komoditi_satuan);
        }

        void setData (Komoditi komoditi) {
            komoditiImg.setImageResource(komoditi.getResourceId());
            komoditiName.setText(komoditi.getKomoditiName());
            komodiitiReward.setText(String.valueOf(komoditi.getKomoditiReward()));
            komoditiRange.setText(context.getString(R.string.komoditi_range_text,
                    komoditi.getMinPrice(), komoditi.getMaxPrice()));
            komoditiSatuan.setText(komoditi.getKomoditiSatuan());
        }
    }
}
