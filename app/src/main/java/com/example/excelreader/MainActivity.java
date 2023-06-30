package com.example.excelreader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("Excelreader");
        String graphList[] = {"Gesamtansicht", "Einzelansicht", "Tabelle"};
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.activity_main1, graphList);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String name = null;
                                                Intent i = null;
                                                switch (position){
                                                    case 0:
                                                        i = new Intent(MainActivity.this, AllGraphics.class);
                                                        break;
                                                    case 1:
                                                        i = new Intent(MainActivity.this, OneGraphic.class);
                                                        break;
                                                    case 2:
                                                        i = new Intent(MainActivity.this, Table.class);
                                                        break;
                                                }
                                                if(i != null) startActivity(i);;
                                            }
                                        }
        );

        }

    }