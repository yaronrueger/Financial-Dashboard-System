package com.example.dashboardmobileapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dashboardmobileapp.Activities.MainActivity;
import com.example.dashboardmobileapp.Activities.SearchActivity;
import com.example.dashboardmobileapp.Domain.SearchWallet;
import com.example.dashboardmobileapp.R;
import com.majorik.sparklinelibrary.SparkLineLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchWalletAdapter extends RecyclerView.Adapter<SearchWalletAdapter.Viewholder> {
    ArrayList<SearchWallet> searchWallets;
    DecimalFormat formatter;
    private Context context;

    public SearchWalletAdapter(Context context,ArrayList<SearchWallet> searchWallets){
        this.context = context;
        this.searchWallets = searchWallets;
        formatter = new DecimalFormat("###,###,###,###");
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_searchwallet, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.cryptoNameText.setText(searchWallets.get(position).getCryptoName());
        holder.cryptoPriceText.setText(searchWallets.get(position).getCryptoPrice().toString()+"€");
        holder.changePercentText.setText(searchWallets.get(position).getChangePercent()+"%");

        if (searchWallets.get(position).getLineData() != null) {
            holder.lineChart.setData(searchWallets.get(position).getLineData());
        } else {
            // Handle the case where arrayData is null
            // For example, set a default or handle the null case appropriately
        }
        if(searchWallets.get(position).getChangePercent()>0){
            holder.changePercentText.setTextColor(Color.parseColor("#12c737"));
        } else if(searchWallets.get(position).getChangePercent()<0){
            holder.changePercentText.setTextColor(Color.parseColor("#fc0000"));
        } else{
            holder.changePercentText.setTextColor(Color.parseColor("#ffffff"));
        }

        int drawableResourceId;
        if(searchWallets.get(position).getStarterpage()){
            drawableResourceId = holder.itemView.getContext()
                    .getResources().getIdentifier("check","drawable",holder.itemView.getContext().getPackageName());
        } else {
           drawableResourceId = holder.itemView.getContext()
                    .getResources().getIdentifier("add","drawable",holder.itemView.getContext().getPackageName());

        }

        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.addLogo);


        holder.addLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int drawableResourceId;
                if(searchWallets.get(position).getStarterpage()){
                    drawableResourceId = holder.itemView.getContext()
                            .getResources().getIdentifier("add","drawable",holder.itemView.getContext().getPackageName());
                    searchWallets.get(position).setStarterpage(false);
                    apiCall(position);

                } else {
                    drawableResourceId = holder.itemView.getContext()
                            .getResources().getIdentifier("check","drawable",holder.itemView.getContext().getPackageName());
                    searchWallets.get(position).setStarterpage(true);
                    apiCall(position);
                }
                Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.addLogo);



            }
        });

    }

    public void apiCall(int position){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String title=searchWallets.get(position).getCryptoName();
        String type =searchWallets.get(position).getType().toLowerCase();
        String url = "https://YOUR-DOMAIN/startpage/favourites/change?title="+ title +"&type="+ type;
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    //
                },
                error -> {
                    // Handle the POST request error
                }
        ) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer " + sharedPreferences.getString("token", ""));
                return headers;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public int getItemCount() {
        return searchWallets.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView cryptoNameText, cryptoPriceText, changePercentText;
        ImageView addLogo;
        SparkLineLayout lineChart;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            cryptoNameText = itemView.findViewById(R.id.addNameText);
            cryptoPriceText = itemView.findViewById(R.id.addPriceText);
            lineChart=itemView.findViewById(R.id.addSparkLineLayout);
            changePercentText = itemView.findViewById(R.id.addchangePercentText);
            addLogo=itemView.findViewById(R.id.addView);
        }
    }
}
