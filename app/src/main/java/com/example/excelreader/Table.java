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
import android.view.View;
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
        TableLayout tableLayout = new TableLayout(getApplicationContext());

        Space space = new Space(this);
        space.setMinimumHeight(50);
        Space spaceSubHeader = new Space(this);
        spaceSubHeader.setMinimumHeight(50);
        Space spaceHeader = new Space(this);
        spaceHeader.setMinimumHeight(50);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout linearLayoutSumOfTheYear = firstBlockExcel.getSumOfTheYearLinearLayout(getApplicationContext());
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
        TableRow monthTableRow1 = firstBlockExcel.getMonthsTableRow1(getApplicationContext());
        TableRow valueTableRow1 = firstBlockExcel.getMonthsValueTableRow1(getApplicationContext());
        //last Months
        TableRow monthTableRow2 = firstBlockExcel.getMonthsTableRow2(getApplicationContext());
        TableRow valueTableRow2 = firstBlockExcel.getMonthsValueTableRow2(getApplicationContext());

        tableLayout.addView(monthTableRow1);
        tableLayout.addView(valueTableRow1);
        tableLayout.addView(monthTableRow2);
        tableLayout.addView(valueTableRow2);
        //set firstBlock to mainDisplay
        linearLayout.addView(tableLayout);

        linearLayout.addView(linearLayoutSumOfTheYear);
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

        for(int i = 0; i<=secondBlockExcel.getSubHeaders().size()-1; i++){
            TextView subHeaderSecondBlock = new TextView(this);
            Space spacebetween = new Space(this);
            spacebetween.setMinimumHeight(50);
            subHeaderSecondBlock.setText(""+(i+1)+"."+secondBlockExcel.getSubHeaders().get(i)+":");
            subHeaderSecondBlock.setTextColor(Color.WHITE);
            subHeaderSecondBlock.setTextSize(14);
            linearLayout.addView(subHeaderSecondBlock);
            linearLayout.addView(spacebetween);

            linearLayout.addView(secondBlockExcel.getSubHeaderValueTable(getApplicationContext(),i));
            linearLayout.addView(secondBlockExcel.getSumOfTheYearLinearLayout(getApplicationContext(),i));

        }

        TextView thirdBlockHeader = new TextView(this);
        thirdBlockHeader.setText(thirdBlockExcel.getBlockHeader());
        thirdBlockHeader.setGravity(Gravity.CENTER);
        thirdBlockHeader.setTextColor(Color.WHITE);
        thirdBlockHeader.setTextSize(16);
        Space spaceHeader3 = new Space(this);
        spaceHeader3.setMinimumHeight(50);

        linearLayout.addView(thirdBlockHeader);
        linearLayout.addView(spaceHeader3);

        //TODO Gesamtsumme einbauen
        for(int i = 0; i <= thirdBlockExcel.getSubHeaders().size()-1;i++){
            TextView subHeaderThirdBlock = new TextView(this);
            Space spacebetween = new Space(this);
            subHeaderThirdBlock.setText(""+(i+1)+"."+thirdBlockExcel.getSubHeaders().get(i)+":");
            subHeaderThirdBlock.setTextColor(Color.WHITE);
            subHeaderThirdBlock.setTextSize(14);
            linearLayout.addView(subHeaderThirdBlock);
            spacebetween.setMinimumHeight(50);
            linearLayout.addView(spacebetween);

            linearLayout.addView(thirdBlockExcel.getSubHeaderValueTable(getApplicationContext(),i));
            linearLayout.addView(thirdBlockExcel.getSumOfTheYearLinearLayout(getApplicationContext(),i));

        }

        scrollView.addView(linearLayout);
    }
}

