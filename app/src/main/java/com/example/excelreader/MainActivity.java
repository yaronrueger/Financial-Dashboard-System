package com.example.excelreader;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean selected = false;
    InputStream data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Excelreader");
        String[] graphList = {"Exceldatei auswählen","Gesamtansicht", "Einzelansicht", "Tabelle"};
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.activity_main1, graphList);


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String name = null;
                                                Intent i = null;
                                                switch (position) {
                                                    case 0:
                                                        selectdata();
                                                        break;
                                                    case 1:
                                                        i = new Intent(MainActivity.this, AllGraphics.class);
                                                        break;
                                                    case 2:
                                                        i = new Intent(MainActivity.this, OneGraphic.class);
                                                        break;
                                                    case 3:
                                                        i = new Intent(MainActivity.this, Table.class);
                                                        break;
                                                }
                                                if(position != 0) {
                                                    if (i != null && selected) {
                                                        startActivity(i);
                                                    } else {
                                                        startError();
                                                    }
                                                }

                                            }
                                        }
        );

    }

    public void selectdata(){
        Intent openFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openFileIntent.setType("*/*");
        startActivityForResult(openFileIntent, 0);
    }
    public void startError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Exceldatei nicht ausgewählt.");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(true);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("close", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            dialog.cancel();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("select File", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            selectdata();
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        Log.e("DataError: ", "Datei nicht ausgewählt oder Fehler beim Laden");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Uri uri = null;
        if (resultData != null) {
            uri = resultData.getData();
            try {
                data = getContentResolver().openInputStream(uri);
                selected = true;
            } catch (FileNotFoundException e) {
                Log.e("ERROR", "File not found lul :)");
            }
        }
    }
}