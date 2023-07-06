package com.example.excelreader;

import java.io.*;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class ExcelRead {
    Workbook workbook;
    Sheet sheet;
    Row row;
    Cell cell;

    static InputStream data;

    public ExcelRead(InputStream is){
        try {
            workbook = new XSSFWorkbook(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sheet = workbook.getSheetAt(0);
    }

    public DataElement getCellData(int zeile, int spalte) {
        row = sheet.getRow(zeile);
        cell = row.getCell(spalte);
        switch (cell.getCellType()) {
            case NUMERIC:
                return new DataElement(cell.getNumericCellValue(), null);
            case STRING:
                return new DataElement(null, cell.getStringCellValue());
            case BOOLEAN:
                return new DataElement(null, cell.getBooleanCellValue() + "");
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case BOOLEAN:
                        return new DataElement(null,cell.getBooleanCellValue() + "");
                    case NUMERIC:
                        return new DataElement(cell.getNumericCellValue(), null);
                    case STRING:
                        return new DataElement(null, cell.getStringCellValue());
                    default:
                        return new DataElement(null,"Formel nicht gelesen");
                }
            case BLANK:
                return new DataElement(null,"");
            default:
                return new DataElement(null,"Wert nicht gelesen");
        }
    }
    //JAN-DEZ
    public ArrayList<String> getMonths(){
        int monthLine = 3;
        ArrayList<String> months = new ArrayList<String>();
        for(int j = 2; j<14; j++){
            months.add(getCellData(monthLine,j).getValueString());
        }
        return months;
    }

    public TableRow getMonthsTableRow1(Context parent){
        TableRow monthTableRow = new TableRow(parent.getApplicationContext());
        for(int i = 0; i <this.getMonths().size()/2; i++){
            TextView tv = new TextView(parent.getApplicationContext());
            tv.setText(this.getMonths().get(i));

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(parent.getResources().getColor(R.color.subtext));
            tv.setGravity(Gravity.CENTER);
            monthTableRow.addView(tv);
        }
        //monthTableRow.setGravity(Gravity.CENTER);
        monthTableRow.setBackgroundColor(parent.getResources().getColor(R.color.months));
        return monthTableRow;
    }

    public TableRow getMonthsTableRow2(Context parent){
        TableRow monthTableRow = new TableRow(parent.getApplicationContext());
        for(int i = this.getMonths().size()/2; i <=this.getMonths().size()-1; i++){
            TextView tv = new TextView(parent.getApplicationContext());
            tv.setText(this.getMonths().get(i));

            TableRow.LayoutParams lp = new TableRow.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(30);
            tv.setLayoutParams(lp);

            tv.setTextColor(parent.getResources().getColor(R.color.subtext));
            tv.setGravity(Gravity.CENTER);
            monthTableRow.addView(tv);
        }
        //monthTableRow.setGravity(Gravity.CENTER);
        monthTableRow.setBackgroundColor(parent.getResources().getColor(R.color.months));
        return monthTableRow;
    }

    //Summe Jahr <akutelles Jahr>
    public String getSumOfYearHeader(){
        return getCellData(3,14).getValueString();
    }

    public abstract String getBlockHeader();

    //TODO Plan Methoden 3x (ganz Rechts)
    }
