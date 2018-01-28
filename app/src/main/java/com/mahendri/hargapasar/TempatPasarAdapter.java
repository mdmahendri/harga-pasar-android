package com.mahendri.hargapasar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahendri.hargapasar.entity.TempatPasar;

import java.util.ArrayList;

/**
 * @author Mahendri Dwicahyo
 */

public class TempatPasarAdapter extends RecyclerView.Adapter<TempatPasarAdapter.TempatViewHolder> {

    public interface TempatClickListener {
        void onTempatClick (int position, String nama);
    }

    private ArrayList<TempatPasar> data;
    private TempatClickListener listener;

    public TempatPasarAdapter (ArrayList<TempatPasar> data, TempatClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public TempatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_place, null);
        return new TempatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TempatViewHolder holder, int position) {
        TempatPasar pasar = data.get(position);
        holder.setData(pasar);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TempatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        private ImageView placeImg;
        private TextView placeTitle;
        private TextView placeKomoditas;
        private TextView placeReward;

        TempatViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            placeImg = itemView.findViewById(R.id.place_img);
            placeTitle = itemView.findViewById(R.id.place_title);
            placeKomoditas = itemView.findViewById(R.id.place_komoditas);
            placeReward = itemView.findViewById(R.id.place_reward);
            itemView.setOnClickListener(this);
        }

        void setData (TempatPasar pasar) {
            placeImg.setImageResource(pasar.getResourceId());
            placeTitle.setText(pasar.getPlaceTitle());
            placeKomoditas.setText(context.getString(R.string.num_komoditas, pasar.getPlaceKomoditas()));
            placeReward.setText(String.valueOf(pasar.getPlaceReward()));
        }

        @Override
        public void onClick(View view) {
            listener.onTempatClick(0, placeTitle.getText().toString());
        }
    }
}
