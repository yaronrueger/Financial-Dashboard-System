package com.example.excelreader;

import java.io.InputStream;
import java.util.ArrayList;

public class ThirdBlockExcel extends ExcelRead{

    public ThirdBlockExcel(InputStream is){
        super(is);
    }

    public String getBlockHeader(){
        return this.getCellData(14, 1).getValueString();
    }

    //SpaltenNamen von Ausgaben
    public ArrayList<String> getSubHeaders(){
        ArrayList<String> subheader = new ArrayList<String>();
        for(int i = 15; i< 53; i++){
            subheader.add(getCellData(i,1).getValueString());
        }
        return subheader;
    }

    //Matrix von den Values der einzelnen Zeilen
    public ArrayList<ArrayList<Double>> getSubHeadersValues(){
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
        for(int i = 2; i<14; i++){
            matrix.add(new ArrayList<>());
            for(int j = 15; j<53; j++){
                matrix.get(i-2).add(getCellData(j,i).getValueDouble());
            }
        }
        return matrix;
    }

    //Summe unter "Summe Jahr <aktuelles Jahr>" Header
    public ArrayList<Double> getSumofYearValue(){
        ArrayList<Double> values = new ArrayList<Double>();
        for(int i = 15; i < 52; i++){
            values.add(getCellData(i,14).getValueDouble());
        }
        return values;
    }
}
