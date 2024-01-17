package com.example.dashboardmobileapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.health.SystemHealthManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dashboardmobileapp.R;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.dashboardmobileapp.Domain.DataWallet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataWalletAdapter extends RecyclerView.Adapter<DataWalletAdapter.Viewholder> {

    ArrayList<DataWallet> dataWallets;
    DecimalFormat formatter;

    LineChart lineChart;
    private Context context;
    private String typeValue;
    TextView header;

    public DataWalletAdapter(Context context,ArrayList<DataWallet> dataWallets, LineChart lineChart, Boolean type, TextView header){
        this.context = context;
        this.dataWallets = dataWallets;
        formatter = new DecimalFormat("###,###,###,###");
        this.lineChart = lineChart;
        this.typeValue = "expenses";
        if(type) this.typeValue = "income";
        this.header = header;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_datawallet, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.cryptoNameText.setText(dataWallets.get(position).getCryptoName());
        holder.cryptoPriceText.setText(dataWallets.get(position).getCryptoPrice().toString()+"€");
        holder.changePercentText.setText(dataWallets.get(position).getChangePercent()+"%");

        if(dataWallets.get(position).getChangePercent()>0){
            holder.changePercentText.setTextColor(Color.parseColor("#12c737"));
        } else if(dataWallets.get(position).getChangePercent()<0){
            holder.changePercentText.setTextColor(Color.parseColor("#fc0000"));
        } else{
            holder.changePercentText.setTextColor(Color.parseColor("#ffffff"));
        }

        holder.datawalletView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCall(position);
            }
        });
    }

    public void apiCall(int position){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String title=dataWallets.get(position).getCryptoName();
        String url = "https://YOUR-DOMAIN/"+this.typeValue+"/specific";
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        setLineChart(jsonResponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {

                }
        ) {

            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer " + sharedPreferences.getString("token", ""));
                return headers;
            }
            @Override
            public byte[] getBody() {
                String body = "{\""+typeValue+"\": \"" +title+ "\"}";
                return body.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(postRequest);
    }

    private void setLineChart(JSONObject jsonResponse) throws JSONException {
        header.setText(new String(jsonResponse.getString("title").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));

        JSONArray intArray = jsonResponse.getJSONArray("yearValues");
        float[] floatArray = new float[intArray.length()];
        for (int i = 0; i < intArray.length(); i++) {
            floatArray[i] = (float) intArray.getInt(i);
        }

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < intArray.length(); i++) {
            entries.add(new Entry(i, floatArray[i]));
        }

        // Add more entries as needed
        LineDataSet dataSet = new LineDataSet(entries, "Cash");
        dataSet.setColor(Color.GREEN); // Set line color
        dataSet.setLineWidth(1f); // Set line width
        dataSet.setCircleColor(Color.GREEN); // Set point color
        dataSet.setCircleRadius(4f); // Set point radius
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Set line mode
        dataSet.setDrawValues(true); // Show values on points
        dataSet.setValueTextColor(Color.WHITE);
        LineData lineData = new LineData(dataSet);
        lineChart.setTouchEnabled(false);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.animateX(1000);
        lineChart.getXAxis().setDrawGridLines(false);
        XAxis xAxis = lineChart.getXAxis();
        YAxis rightYAxis = lineChart.getAxisRight();
        YAxis leftYAxis = lineChart.getAxisLeft();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(
                new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}
        )); // Set custom labels for the x-axis
        xAxis.setTextColor(Color.WHITE); // Set the color of the x-axis to white
        xAxis.setDrawGridLines(false);
        leftYAxis.setDrawAxisLine(false);
        rightYAxis.setEnabled(false); // Disable the right y-axis
        leftYAxis.setTextColor(Color.WHITE); // Set the color of the left y-axis to white
        leftYAxis.setEnabled(true);
        leftYAxis.setDrawLabels(false);
        lineChart.getDescription().setEnabled(false); // Disable the description label
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position to bottom
        lineChart.getXAxis().setGranularity(1f); // Set the granularity to 1 to show all labels
        lineChart.getXAxis().setLabelCount(12); // Set the number of labels to display
        lineChart.invalidate();
    }


    private int[] makeResponse(JSONObject jsonResponse) {
        return new int[]{0, 1};
    }


    @Override
    public int getItemCount() {
        return dataWallets.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView cryptoNameText, cryptoPriceText, changePercentText;
        ConstraintLayout datawalletView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            datawalletView = itemView.findViewById(R.id.datawallet);
            cryptoNameText = itemView.findViewById(R.id.cryptoNameText);
            cryptoPriceText = itemView.findViewById(R.id.cryptoPriceText);
            changePercentText = itemView.findViewById(R.id.changePercentText);
        }

    }
}
