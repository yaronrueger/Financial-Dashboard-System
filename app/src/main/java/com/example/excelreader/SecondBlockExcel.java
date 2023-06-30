package com.example.excelreader;

import java.io.InputStream;
import java.util.ArrayList;

public class SecondBlockExcel extends ExcelRead{

    public SecondBlockExcel(InputStream is){
        super(is);
    }

    //"Art der Einkünfte"
    public String getBlockHeader(){
        return this.getCellData(6, 1).getValueString();
    }

    //SpaltenNamen von Art der Einkünfte
    public ArrayList<String> getSubHeaders(){
        ArrayList<String> subheader = new ArrayList<String>();
        for(int i = 7; i< 13; i++){
            subheader.add(getCellData(i,1).getValueString());
        }
        return subheader;
    }

    //Matrix von den Values der einzelnen Zeilen
    public ArrayList<ArrayList<Double>> getSubHeadersValues(){
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
        for(int i = 2; i<14; i++){
            matrix.add(new ArrayList<>());
            for(int j = 7; j<13; j++){
                matrix.get(i-2).add(getCellData(j,i).getValueDouble());
            }
        }
        return matrix;
    }

    //Summe unter "Summe Jahr <aktuelles Jahr>" Header
    public ArrayList<Double> getSumofYearValue(){
        ArrayList<Double> values = new ArrayList<Double>();
        for(int i = 7; i < 12; i++){
            values.add(getCellData(i,14).getValueDouble());
        }
        return values;
    }

}
