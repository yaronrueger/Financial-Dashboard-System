package com.example.excelreader;


import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class MainActivity extends AppCompatActivity {

    boolean selected = false;
    InputStream data;
    private SharedPreferences date;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Excelreader");
        String[] graphList = {"Gesamtansicht", "Einzelansicht", "Tabelle"};
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.activity_main1, graphList);

        String fileName = "mappe1.xlsx";
        File file = new File(this.getFilesDir(), fileName);

        date = PreferenceManager.getDefaultSharedPreferences(this);
        editor = date.edit();

        if (file.exists()) {
            TextView textView = findViewById(R.id.textViewAdd);
            textView.setText("Datei ausgewählt \n Stand: " + date.getString("date",""));
            selected = true;
        }

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectdata();
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String name = null;
                                                Intent i = null;
                                                switch (position) {
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
                                                    if (i != null && selected) {
                                                        startActivity(i);
                                                    } else {
                                                        startError();
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

                String fileName = "mappe1.xlsx";
                FileOutputStream fos = this.openFileOutput(fileName, Context.MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = data.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();

                selected = true;
                LocalDate currentTime = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String time = currentTime.format(formatter).toString();
                TextView textView = findViewById(R.id.textViewAdd);
                editor.putString("date", time);
                editor.apply();
                textView.setText("Datei ausgewählt \n Stand: " + date.getString("date",""));
            } catch (FileNotFoundException e) {
                Log.e("ERROR", "File not found lul :)");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
}