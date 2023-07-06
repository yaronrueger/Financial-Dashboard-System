package com.example.excelreader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.InputStream;
import java.util.ArrayList;

public class SecondBlockExcel extends ExcelRead{

    public SecondBlockExcel(InputStream is){
        super(is);
    }

    //"Art der Einkünfte"
    public String getBlockHeader(){
        return this.getCellData(6, 1).getValueString();
    }

    //SpaltenNamen von Art der Einkünfte
    public ArrayList<String> getSubHeaders(){
        ArrayList<String> subheader = new ArrayList<String>();
        for(int i = 7; i< 13; i++){
            subheader.add(getCellData(i,1).getValueString());
        }
        return subheader;
    }

    //Matrix von den Values der einzelnen Zeilen
    public ArrayList<ArrayList<Double>> getSubHeadersValues(){
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
        for(int i = 7; i<13; i++){
            matrix.add(new ArrayList<>());
            for(int j = 2; j<14; j++){
                matrix.get(i-7).add(getCellData(i,j).getValueDouble());
            }
        }
        return matrix;
    }

    //Summe unter "Summe Jahr <aktuelles Jahr>" Header
    public ArrayList<Double> getSumOfYearValue(){
        ArrayList<Double> values = new ArrayList<Double>();
        for(int i = 7; i <= 12; i++){
            values.add(getCellData(i,14).getValueDouble());
        }
        return values;
    }

    public LinearLayout getSumOfTheYearLinearLayout(Context parent, int i){
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

        sumofMonthsValue.setText(""+getSumOfYearValue().get(i));
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
        if(getSumOfYearValue().get(i) == 0){
            sumofMonthsValue.setBackgroundColor(Color.GRAY);
        }
        if(getSumOfYearValue().get(i) > 0) {
            sumofMonthsValue.setBackgroundColor(Color.GREEN);
        }
        if(getSumOfYearValue().get(i) < 0){
            sumofMonthsValue.setBackgroundColor(Color.RED);
            sumofMonthsValue.setTextColor(Color.WHITE);
        }
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        Space space = new Space(parent.getApplicationContext());
        space.setMinimumHeight(1000);
        linearLayout.setPadding(0,0,0,100);

        return linearLayout;
    }

    public TableLayout getSubHeaderValueTable(Context parent, int zeile){
        TableLayout tableLayout = new TableLayout(parent.getApplicationContext());
        TableRow months1 = getMonthsTableRow1(parent.getApplicationContext());
        TableRow months2 = getMonthsTableRow2(parent.getApplicationContext());
        TableRow monthTableRowValue1 = new TableRow(parent.getApplicationContext());
        TableRow monthTableRowValue2 = new TableRow(parent.getApplicationContext());
        months1.setGravity(Gravity.CENTER);
        months2.setGravity(Gravity.CENTER);
        tableLayout.addView(months1);
        for(int i = 0; i <=this.getMonths().size()/2-1; i++){
            TextView tv = new TextView(parent.getApplicationContext());
            tv.setText((String)getSubHeadersValues().get(zeile).get(i).toString());

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(parent.getResources().getColor(R.color.white));
            tv.setGravity(Gravity.CENTER);
            monthTableRowValue1.addView(tv);
        }
        monthTableRowValue1.setGravity(Gravity.CENTER);
        monthTableRowValue1.setBackgroundColor(parent.getResources().getColor(R.color.values));
        tableLayout.addView(monthTableRowValue1);
        tableLayout.addView(months2);

        for(int i = getMonths().size()/2; i <=getMonths().size()-1; i++){
            TextView tv = new TextView(parent.getApplicationContext());
            tv.setText((String)getSubHeadersValues().get(zeile).get(i).toString());

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(parent.getResources().getColor(R.color.white));
            tv.setGravity(Gravity.CENTER);
            monthTableRowValue2.addView(tv);
        }
        monthTableRowValue2.setGravity(Gravity.CENTER);
        monthTableRowValue2.setBackgroundColor(parent.getResources().getColor(R.color.values));
        tableLayout.addView(monthTableRowValue2);
        return tableLayout;
    }

}
