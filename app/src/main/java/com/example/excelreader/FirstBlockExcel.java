package com.example.excelreader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
}
