package com.example.excelreader;

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
}
