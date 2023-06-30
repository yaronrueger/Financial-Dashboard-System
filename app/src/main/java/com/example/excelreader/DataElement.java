package com.example.excelreader;

public class DataElement {
    private String s;
    private Double d;
    private boolean isDouble;

    public DataElement(Double dNew, String sNew){
        s = sNew;
        d = dNew;
        isDouble = true;
        if( s != null){
            isDouble = false;
        }
    }

    public double getValueDouble(){
        return d;
    }

    public String getValueString(){
        return s;
    }

    public boolean getIsDouble(){
        return isDouble;
    }
}
