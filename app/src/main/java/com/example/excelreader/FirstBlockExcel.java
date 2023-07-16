package com.example.excelreader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.poi.sl.usermodel.Line;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.ArrayList;

public class FirstBlockExcel extends ExcelRead {

    public FirstBlockExcel(InputStream is){
        super(is);
    }

    //Verfügbares Barvermögen
    public String getBlockHeader(){
        return this.getCellData(3,1).getValueString();
    }

    //Monatliches Barvermögen
    public String getMonthMoneyHeader(){
        return this.getCellData(4,1).getValueString();
    }

    //Values pro Monat
    public ArrayList<Double> getMonthMoneyValues(){
        int dataLine = 4;
        ArrayList<Double> data = new ArrayList<Double>();
        for(int j = 2; j<14; j++){
            data.add(getCellData(dataLine,j).getValueDouble());
        }
        return data;
    }
    //Summe unter "Summe Jahr <aktuelles Jahr>" Header
    public double getSumOfYearValue(){
        return getCellData(4,14).getValueDouble();
    }

    public TableRow getMonthsValueTableRow1(Context parent){
        TableRow valueTableRow1 = new TableRow(parent.getApplicationContext());
        for(int i = 0; i <getMonths().size()/2; i++){
            TextView tv = new TextView(parent.getApplicationContext());
            tv.setText((String) getMonthMoneyValues().get(i).toString());

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            valueTableRow1.addView(tv);
        }
        //valueTableRow1.setGravity(Gravity.CENTER);
        valueTableRow1.setBackgroundColor(parent.getResources().getColor(R.color.values));
        return valueTableRow1;
    }

    public TableRow getMonthsValueTableRow2(Context parent){
        TableRow valueTableRow2 = new TableRow(parent.getApplicationContext());
        for(int i = getMonths().size()/2; i <=getMonths().size()-1; i++){
            TextView tv = new TextView(parent.getApplicationContext());
            tv.setText((String) getMonthMoneyValues().get(i).toString());

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            valueTableRow2.addView(tv);
        }
        //valueTableRow2.setGravity(Gravity.CENTER);
        valueTableRow2.setBackgroundColor(parent.getResources().getColor(R.color.values));
        return valueTableRow2;
    }

    public LinearLayout getSumOfTheYearLinearLayout(Context parent){
        LinearLayout linearLayout = new LinearLayout(parent.getApplicationContext());
        TextView sumofMonthsValue = new TextView(parent.getApplicationContext());
        TextView sumofMonths = new TextView(parent.getApplicationContext());

        sumofMonths.setText(getSumOfYearHeader()+":");
        sumofMonths.setTextColor(parent.getResources().getColor(R.color.subtext));
        sumofMonths.setBackgroundColor(parent.getResources().getColor(R.color.months));
        sumofMonths.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        sumofMonths.setGravity(Gravity.CENTER);

        sumofMonthsValue.setText(""+getSumOfYearValue());
        sumofMonthsValue.setTextColor(Color.BLACK);
        sumofMonthsValue.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        sumofMonthsValue.setGravity(Gravity.CENTER);
        sumofMonthsValue.setTypeface(null, Typeface.BOLD);

        linearLayout.addView(sumofMonths);
        linearLayout.addView(sumofMonthsValue);
        if(getSumOfYearValue() == 0){
            sumofMonthsValue.setBackgroundColor(Color.GRAY);
        }
        if(getSumOfYearValue() > 0) {
            sumofMonthsValue.setBackgroundColor(Color.GREEN);
        }
        if(getSumOfYearValue() < 0){
            sumofMonthsValue.setBackgroundColor(Color.RED);
            sumofMonthsValue.setTextColor(Color.WHITE);
        }
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    public BarChart getBarChart(Context parent) {

        BarChart barChart = new BarChart(parent);
        XAxis xAxis = barChart.getXAxis();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] barLabels = getMonths().toArray(new String[0]);

        YAxis leftYAxis = barChart.getAxisLeft();
        YAxis rightYAxis = barChart.getAxisRight();

        for (int i = 0; i <= getMonths().size() - 1; i++) {
            entries.add(new BarEntry(i, getMonthMoneyValues().get(i).floatValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSet.setStackLabels(barLabels);
        dataSet.setValueTextColor(Color.WHITE);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.setLayoutParams(new ViewGroup.LayoutParams(950, 950));

        xAxis.setValueFormatter(new IndexAxisValueFormatter(barLabels));
        xAxis.setLabelCount(barLabels.length);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelRotationAngle(300f);

        leftYAxis.setTextColor(Color.WHITE);
        rightYAxis.setEnabled(false);

        barChart.setFitBars(true);

        // Deaktiviere das Farbregister
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        barChart.invalidate();
        barChart.animateY(1400, Easing.EaseInCubic);


        return barChart;
    }

    public LinearLayout getMonthMoneyLinearLayout(Context parent){
        LinearLayout linearLayout = new LinearLayout(parent.getApplicationContext());
        Space space = new Space(parent.getApplicationContext());
        Space space2 = new Space(parent.getApplicationContext());
        TextView textView = new TextView(parent);

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(0, 50, 0, 0);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        textView.setText("Monatseinkommen");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(20);
        shape.setColor(Color.BLACK);
        linearLayout.setBackground(shape);
        linearLayout.addView(textView);
        space2.setMinimumHeight(50);
        linearLayout.addView(space2);
        linearLayout.addView(getBarChart(parent));
        space.setMinimumHeight(50);
        linearLayout.addView(space);
        return linearLayout;
    }
}
