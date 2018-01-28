package com.mahendri.hargapasar.unused;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahendri.hargapasar.R;

import java.util.ArrayList;

/**
 * @author Mahendri Dwicahyo
 */

public class UsulTempatAdapter extends RecyclerView.Adapter<UsulTempatAdapter.UsulTempatHolder> {

    private ArrayList<UsulTempat> data;

    public UsulTempatAdapter (ArrayList<UsulTempat> data) {
        this.data = data;
    }

    @Override
    public UsulTempatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_usul_tempat, null);
        return new UsulTempatHolder(v);
    }

    @Override
    public void onBindViewHolder(UsulTempatHolder holder, int position) {
        UsulTempat tempat = data.get(position);
        holder.setData(tempat);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class UsulTempatHolder extends RecyclerView.ViewHolder {

        private Context context;
        private TextView placeApprove;
        private ImageView placeImg;
        private TextView placeTitle;
        private TextView placeAddress;

        UsulTempatHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            placeApprove = itemView.findViewById(R.id.place_approve);
            placeImg = itemView.findViewById(R.id.place_img);
            placeTitle = itemView.findViewById(R.id.place_title);
            placeAddress = itemView.findViewById(R.id.place_address);
        }

        void setData (UsulTempat tempat) {
            placeApprove.setText(String.valueOf(tempat.getPlaceApprove()));
            placeImg.setImageResource(tempat.getResourceId());
            placeTitle.setText(tempat.getPlaceTitle());
            placeAddress.setText(tempat.getPlaceAddress());

            int color;
            if (tempat.getPlaceApprove() == 0)
                color = android.R.color.darker_gray;
            else if (tempat.getPlaceApprove() > 0)
                color = android.R.color.holo_green_dark;
            else color = android.R.color.holo_red_dark;
            placeApprove.setBackgroundColor(context.getResources().getColor(color));
        }
    }
}
