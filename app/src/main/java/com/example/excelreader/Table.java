package com.example.excelreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class Table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        setTitle("Table");

        TextView text = (TextView) findViewById(R.id.textView2);
        ExcelRead er = new ExcelRead(getAssets(), getResources().openRawResource(R.raw.mappe1));
        text.setText(er.getData());
    }
}