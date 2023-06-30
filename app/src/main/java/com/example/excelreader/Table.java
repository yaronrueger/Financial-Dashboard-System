package com.example.excelreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.poi.ss.formula.functions.T;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;

public class Table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        setTitle("Table");

        FirstBlockExcel firstBlockExcel;
        SecondBlockExcel secondBlockExcel;
        ThirdBlockExcel thirdBlockExcel;
        firstBlockExcel = new FirstBlockExcel(getResources().openRawResource(R.raw.mappe1));
        secondBlockExcel = new SecondBlockExcel(getResources().openRawResource(R.raw.mappe1));
        thirdBlockExcel = new ThirdBlockExcel(getResources().openRawResource(R.raw.mappe1));



        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollLayout);

        TextView header = new TextView(getApplicationContext());
        TextView subHeader = new TextView(getApplicationContext());
        TextView sumofMonths = new TextView(getApplicationContext());
        TextView sumofMonthsValue = new TextView(getApplicationContext());
        TableLayout tableLayout = new TableLayout(getApplicationContext());

        Space space = new Space(this);
        space.setMinimumHeight(50);
        Space spaceSubHeader = new Space(this);
        spaceSubHeader.setMinimumHeight(50);
        Space spaceHeader = new Space(this);
        spaceHeader.setMinimumHeight(50);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout linearLayoutHorizontal = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        header.setText(firstBlockExcel.getBlockHeader());
        header.setTextColor(Color.WHITE);
        header.setTextSize(16);
        header.setGravity(Gravity.CENTER);

        linearLayout.addView(header);
        linearLayout.addView(spaceHeader);

        subHeader.setText("1."+firstBlockExcel.getMonthMoneyHeader()+":");
        subHeader.setTextColor(Color.WHITE);
        subHeader.setTextSize(14);

        linearLayout.addView(subHeader);
        linearLayout.addView(spaceSubHeader);


        //set Months and Values:
        //first Months
        TableRow monthTableRow1 = new TableRow(this);
        TableRow valueTableRow1 = new TableRow(this);
        //last Months
        TableRow monthTableRow2 = new TableRow(this);
        TableRow valueTableRow2 = new TableRow(this);
        //setup first Months
        for(int i = 0; i <firstBlockExcel.getMonths().size()/2; i++){
            TextView tv = new TextView(getApplicationContext());
            tv.setText(firstBlockExcel.getMonths().get(i));

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(getResources().getColor(R.color.subtext));
            monthTableRow1.addView(tv);
        }

        monthTableRow1.setGravity(Gravity.CENTER);
        monthTableRow1.setBackgroundColor(getResources().getColor(R.color.months));
        tableLayout.addView(monthTableRow1);

        //setup first MonthsValues
        for(int i = 0; i <firstBlockExcel.getMonths().size()/2; i++){
            TextView tv = new TextView(getApplicationContext());
            tv.setText((String) firstBlockExcel.getMonthMoneyValues().get(i).toString());

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(Color.WHITE);
            valueTableRow1.addView(tv);
        }
        //some Frontendstuff
        valueTableRow1.setGravity(Gravity.CENTER);
        valueTableRow1.setBackgroundColor(getResources().getColor(R.color.values));
        tableLayout.addView(valueTableRow1);
        //setup last Months
        for(int i = firstBlockExcel.getMonths().size()/2; i <=firstBlockExcel.getMonths().size()-1; i++){
            TextView tv = new TextView(getApplicationContext());
            tv.setText(firstBlockExcel.getMonths().get(i));

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(getResources().getColor(R.color.subtext));
            monthTableRow2.addView(tv);
        }

        monthTableRow2.setGravity(Gravity.CENTER);
        monthTableRow2.setBackgroundColor(getResources().getColor(R.color.months));
        tableLayout.addView(monthTableRow2);

        //setup last MonthsValues
        for(int i = firstBlockExcel.getMonths().size()/2; i <=firstBlockExcel.getMonths().size()-1; i++){
            TextView tv = new TextView(getApplicationContext());
            tv.setText((String) firstBlockExcel.getMonthMoneyValues().get(i).toString());

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(Color.WHITE);
            valueTableRow2.addView(tv);
        }
        //some Frontendstuff
        valueTableRow2.setGravity(Gravity.CENTER);
        valueTableRow2.setBackgroundColor(getResources().getColor(R.color.values));
        tableLayout.addView(valueTableRow2);
        //set firstBlock to mainDisplay
        linearLayout.addView(tableLayout);

        sumofMonths.setText(firstBlockExcel.getSumOfYearHeader()+":");
        sumofMonths.setTextColor(getResources().getColor(R.color.subtext));
        sumofMonths.setBackgroundColor(getResources().getColor(R.color.months));
        sumofMonths.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        sumofMonths.setGravity(Gravity.CENTER);

        sumofMonthsValue.setText(""+firstBlockExcel.getSumOfYearValue());
        sumofMonthsValue.setTextColor(Color.BLACK);
        sumofMonthsValue.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        sumofMonthsValue.setGravity(Gravity.CENTER);
        sumofMonthsValue.setTypeface(null, Typeface.BOLD);

        linearLayoutHorizontal.addView(sumofMonths);
        linearLayoutHorizontal.addView(sumofMonthsValue);
        sumofMonthsValue.setBackgroundColor(Color.GREEN);
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHorizontal.setGravity(Gravity.CENTER);
        linearLayout.addView(linearLayoutHorizontal);

        linearLayout.addView(space);

        TextView secondBlockHeader = new TextView(this);
        secondBlockHeader.setText(secondBlockExcel.getBlockHeader());
        secondBlockHeader.setGravity(Gravity.CENTER);
        secondBlockHeader.setTextColor(Color.WHITE);
        secondBlockHeader.setTextSize(16);
        Space spaceHeader2 = new Space(this);
        spaceHeader2.setMinimumHeight(50);

        linearLayout.addView(secondBlockHeader);
        linearLayout.addView(spaceHeader2);


        ArrayList<String> secondBlocksubHeaders = new ArrayList<String>();
        secondBlocksubHeaders = secondBlockExcel.getSubHeaders();


        TextView subHeader1 = new TextView(this);
        Space spacesubHeader1 = new Space(this);
        spacesubHeader1.setMinimumHeight(50);
        TextView subHeader2 = new TextView(this);
        Space spacesubHeader2 = new Space(this);
        spacesubHeader2.setMinimumHeight(50);
        TextView subHeader3 = new TextView(this);
        Space spacesubHeader3 = new Space(this);
        spacesubHeader3.setMinimumHeight(50);
        TextView subHeader4 = new TextView(this);
        Space spacesubHeader4 = new Space(this);
        spacesubHeader4.setMinimumHeight(50);
        TextView subHeader5 = new TextView(this);
        Space spacesubHeader5 = new Space(this);
        spacesubHeader5.setMinimumHeight(50);
        TextView subHeader6 = new TextView(this);
        Space spacesubHeader6 = new Space(this);
        spacesubHeader6.setMinimumHeight(50);

        subHeader1.setText("1."+secondBlockExcel.getSubHeaders().get(0)+":");
        subHeader1.setTextColor(Color.WHITE);
        subHeader1.setTextSize(14);
        subHeader2.setText("2."+secondBlockExcel.getSubHeaders().get(1)+":");
        subHeader2.setTextColor(Color.WHITE);
        subHeader2.setTextSize(14);
        subHeader3.setText("3."+secondBlockExcel.getSubHeaders().get(2)+":");
        subHeader3.setTextColor(Color.WHITE);
        subHeader3.setTextSize(14);
        subHeader4.setText("4."+secondBlockExcel.getSubHeaders().get(3)+":");
        subHeader4.setTextColor(Color.WHITE);
        subHeader4.setTextSize(14);
        subHeader5.setText("5."+secondBlockExcel.getSubHeaders().get(4)+":");
        subHeader5.setTextColor(Color.WHITE);
        subHeader5.setTextSize(14);
        subHeader6.setText("6."+secondBlockExcel.getSubHeaders().get(5)+":");
        subHeader6.setTextColor(Color.WHITE);
        subHeader6.setTextSize(14);

        linearLayout.addView(subHeader1);
        linearLayout.addView(spacesubHeader1);

        linearLayout.addView(subHeader2);
        linearLayout.addView(spacesubHeader2);

        linearLayout.addView(subHeader3);
        linearLayout.addView(spacesubHeader3);

        linearLayout.addView(subHeader4);
        linearLayout.addView(spacesubHeader4);

        linearLayout.addView(subHeader5);
        linearLayout.addView(spacesubHeader5);

        linearLayout.addView(subHeader6);
        linearLayout.addView(spacesubHeader6);




        TextView thirdBlockHeader = new TextView(this);
        thirdBlockHeader.setText(thirdBlockExcel.getBlockHeader());
        thirdBlockHeader.setGravity(Gravity.CENTER);
        thirdBlockHeader.setTextColor(Color.WHITE);
        thirdBlockHeader.setTextSize(16);
        Space spaceHeader3 = new Space(this);
        spaceHeader3.setMinimumHeight(50);

        linearLayout.addView(thirdBlockHeader);
        linearLayout.addView(spaceHeader3);

        for(int i = 0; i <= thirdBlockExcel.getSubHeaders().size()-1;i++){
            TextView subHeaderThirdBlock = new TextView(this);
            subHeaderThirdBlock.setText(""+(i+1)+"."+thirdBlockExcel.getSubHeaders().get(i));
            subHeaderThirdBlock.setTextColor(Color.WHITE);
            subHeaderThirdBlock.setTextSize(14);
            linearLayout.addView(subHeaderThirdBlock);
            Space spacebetween = new Space(this);
            spacebetween.setMinimumHeight(50);
            linearLayout.addView(spacebetween);
        }
        //TODO Summe gesamtes Jahr einzeln ändern



        scrollView.addView(linearLayout);
    }
}

