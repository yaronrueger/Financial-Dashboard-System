package com.example.excelreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.graphics.Color;

import android.os.Bundle;

import android.view.Gravity;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class AllGraphics extends AppCompatActivity {

    private PieChart pieChart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    FirstBlockExcel firstBlockExcel;
    SecondBlockExcel secondBlockExcel;
    ThirdBlockExcel thirdBlockExcel;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_graphics);

        firstBlockExcel = new FirstBlockExcel(getResources().openRawResource(R.raw.mappe1));
        secondBlockExcel = new SecondBlockExcel(getResources().openRawResource(R.raw.mappe1));
        thirdBlockExcel = new ThirdBlockExcel(getResources().openRawResource(R.raw.mappe1));
        scrollView = findViewById(R.id.scrollView);

        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setGravity(Gravity.CENTER);
        pieChart = new PieChart(getApplicationContext());
        pieChart.setLayoutParams(new ViewGroup.LayoutParams(900, 900));
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setDrawEntryLabels(false);
        setupPieChart();
        loadPieChartData();

        linearLayout.addView(pieChart);
        scrollView.addView(linearLayout);

        /*
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint);
        constraintLayoutInner = new ConstraintLayout(this);

        pieChart = new PieChart(getApplicationContext());
        pieChart.setLayoutParams(new ViewGroup.LayoutParams(900, 900));
        constraintLayout.setForegroundGravity(Gravity.CENTER);
        constraintLayoutInner.setPadding(300,200,0,0);

        constraintLayoutInner.addView(pieChart);
        constraintLayout.addView(constraintLayoutInner);
        setupPieChart();
        loadPieChartData();
         */
    }
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        //pieChart.setEntryLabelTextSize(9);
        //pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setCenterText("Spending by Category");
        //pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        l.setTextColor(Color.WHITE);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(10, "Einkommen 1"));
        entries.add(new PieEntry(10, "Einkommen 2"));
        entries.add(new PieEntry(10, "Einkommen 3"));
        entries.add(new PieEntry(10, "Einkommen 4"));
        entries.add(new PieEntry(10, "Einkommen 5"));
        entries.add(new PieEntry(50, "Einkommen 6"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.JOYFUL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "dataSet");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}