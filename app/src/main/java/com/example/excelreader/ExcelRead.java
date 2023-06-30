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
import android.content.res.AssetManager;

public abstract class ExcelRead {
    Workbook workbook;
    Sheet sheet;
    Row row;
    Cell cell;

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

    //Summe Jahr <akutelles Jahr>
    public String getSumOfYearHeader(){
        return getCellData(3,14).getValueString();
    }

    public abstract String getBlockHeader();

    //TODO Plan Methoden 3x (ganz Rechts)

    /*
     public int getlengthRowAll(){
        return sheet.getLastRowNum() +1;
    }
     */

   /*
    public int getLengthOneRow(int i){
        row = sheet.getRow(i);
        return row.getLastCellNum();
    }
    */
/*
    public ArrayList<ArrayList<String>> getSheet(){
        ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();
        for(int i = 0; i < sheet.getLastRowNum() +1; i++) {
            row = sheet.getRow(i);
            matrix.add(new ArrayList<String>());
            for(int j = 0; j < row.getLastCellNum(); j++){
                cell = row.getCell(j);
                matrix.get(i).add(getCellData(cell));
             }
         }
         return matrix;
    }
*/
    /*
    public ArrayList<String> getSheetRow(int rowNum){
        ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();
        matrix = this.getSheet();
        ArrayList<String> matrixRow = new ArrayList<String>();
        matrixRow = matrix.get(rowNum);
        return matrixRow;
    }
     */

    /*
    public String getSheetCell(int zeile, int spalte){
        ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();
        ArrayList<String> matrixRow = new ArrayList<String>();
        String matrixCell;
        matrix = this.getSheet();
        matrixRow = matrix.get(zeile);
        matrixCell = matrixRow.get(spalte);
        return matrixCell;
    }
     */
    }
