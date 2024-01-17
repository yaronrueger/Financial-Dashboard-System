package com.example.dashboardmobileapp.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dashboardmobileapp.Domain.CryptoWallet;
import com.example.dashboardmobileapp.R;
import com.majorik.sparklinelibrary.SparkLineLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CryptoWalletAdapter extends RecyclerView.Adapter<CryptoWalletAdapter.Viewholder> {
    ArrayList<CryptoWallet> cryptoWallets;
    DecimalFormat formatter;

    public CryptoWalletAdapter(ArrayList<CryptoWallet> cryptoWallets){
        this.cryptoWallets = cryptoWallets;
        formatter = new DecimalFormat("###,###,###,###");
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wallet, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.cryptoNameText.setText(cryptoWallets.get(position).getCryptoName());
        holder.cryptoPriceText.setText(cryptoWallets.get(position).getCryptoPrice().toString()+"€");
        holder.changePercentText.setText(cryptoWallets.get(position).getChangePercent()+"%");

        if (cryptoWallets.get(position).getLineData() != null) {
            holder.lineChart.setData(cryptoWallets.get(position).getLineData());
        } else {
            // Handle the case where arrayData is null
            // For example, set a default or handle the null case appropriately
        }
        if(cryptoWallets.get(position).getChangePercent()>0){
            holder.changePercentText.setTextColor(Color.parseColor("#12c737"));
        } else if(cryptoWallets.get(position).getChangePercent()<0){
            holder.changePercentText.setTextColor(Color.parseColor("#fc0000"));
        } else{
            holder.changePercentText.setTextColor(Color.parseColor("#ffffff"));
        }

        int drawableResourceId=holder.itemView.getContext()
                .getResources().getIdentifier(cryptoWallets.get(position).getType(),"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.logoCrypto);
    }

    @Override
    public int getItemCount() {
        return cryptoWallets.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView cryptoNameText, cryptoPriceText, changePercentText;
        ImageView logoCrypto;
        SparkLineLayout lineChart;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            cryptoNameText = itemView.findViewById(R.id.cryptoNameText);
            cryptoPriceText = itemView.findViewById(R.id.cryptoPriceText);
            changePercentText = itemView.findViewById(R.id.changePercentText);
            logoCrypto=itemView.findViewById(R.id.logoImg);
            lineChart=itemView.findViewById(R.id.sparkLineLayout);
        }
    }
}
