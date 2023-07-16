package com.example.excelreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.poi.sl.usermodel.Line;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AllGraphics extends AppCompatActivity {

    FirstBlockExcel firstBlockExcel;
    SecondBlockExcel secondBlockExcel;
    ThirdBlockExcel thirdBlockExcel;
    ScrollView scrollView;
    LinearLayout linearLayoutBarCharts;
    LinearLayout linearLayoutPieCharts;
    LinearLayout linearLayoutMain;
    Button buttonBar;
    Button buttonPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_graphics);

        linearLayoutMain = new LinearLayout(this);
        linearLayoutMain.setOrientation(LinearLayout.VERTICAL);
        linearLayoutMain.setGravity(Gravity.CENTER);

        buttonBar = new Button(this);
        buttonBar.setGravity(Gravity.CENTER);
        buttonBar.setText("Show Bars");
        buttonBar.setBackgroundColor(getResources().getColor(R.color.elements));
        buttonBar.setTextColor(Color.WHITE);
        buttonBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutVisible(linearLayoutBarCharts);
            }
        });

        buttonPie = new Button(this);
        buttonPie.setGravity(Gravity.CENTER);
        buttonPie.setText("Show Pies");
        buttonPie.setBackgroundColor(getResources().getColor(R.color.elements));
        buttonPie.setTextColor(Color.WHITE);
        buttonPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutVisible(linearLayoutPieCharts);
            }
        });

        linearLayoutBarCharts = new LinearLayout(getApplicationContext());
        linearLayoutBarCharts.setOrientation(LinearLayout.VERTICAL);
        linearLayoutBarCharts.setGravity(Gravity.CENTER);
        try {
            firstBlockExcel = new FirstBlockExcel(getApplicationContext().openFileInput("mappe1.xlsx"));
            secondBlockExcel = new SecondBlockExcel(getApplicationContext().openFileInput("mappe1.xlsx"));
            thirdBlockExcel = new ThirdBlockExcel(getApplicationContext().openFileInput("mappe1.xlsx"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        scrollView = findViewById(R.id.scrollView);
        scrollView.setPadding(100, 0, 95, 0);
        scrollView.setVerticalScrollBarEnabled(false);

        linearLayoutBarCharts.addView(firstBlockExcel.getMonthMoneyLinearLayout(getApplicationContext()));
        linearLayoutBarCharts.addView(getSpace(this));
        for(int i = 0; i<=secondBlockExcel.getSubHeadersValues().size()-1; i++){
            linearLayoutBarCharts.addView(secondBlockExcel.getMonthMoneyLinearLayout(i,getApplicationContext()));
            linearLayoutBarCharts.addView(getSpace(this));
        }
        for(int i = 0; i<=thirdBlockExcel.getSubHeadersValues().size()-1; i++){
            linearLayoutBarCharts.addView(thirdBlockExcel.getMonthMoneyLinearLayout(i,getApplicationContext()));
            linearLayoutBarCharts.addView(getSpace(this));
        }

        linearLayoutMain.addView(buttonBar);
        linearLayoutMain.addView(getSpace(this));
        linearLayoutMain.addView(linearLayoutBarCharts);
        layoutVisible(linearLayoutBarCharts);

        linearLayoutPieCharts = new LinearLayout(this);
        linearLayoutPieCharts.setOrientation(LinearLayout.VERTICAL);
        linearLayoutPieCharts.setGravity(Gravity.CENTER);
        linearLayoutPieCharts.addView(firstBlockExcel.getMonthMoneyLinearLayout(this));

        linearLayoutMain.addView(buttonPie);
        linearLayoutMain.addView(getSpace(this));
        linearLayoutMain.addView(linearLayoutPieCharts);
        layoutVisible(linearLayoutPieCharts);

        scrollView.addView(linearLayoutMain);
    }

    public Space getSpace(Context parent){
        Space space = new Space(parent);
        space.setMinimumHeight(60);
        return space;
    }
    public void layoutVisible(LinearLayout linearLayout){
        if(linearLayout.getVisibility() == View.VISIBLE){
            linearLayout.setVisibility(View.GONE);
        }
        else{
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}