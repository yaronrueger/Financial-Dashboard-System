package com.example.excelreader;

import java.io.*;

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

public class ExcelRead {
    InputStream file;

    POIFSFileSystem file2;
    Workbook workbook;
    Sheet sheet;
    CellAddress cellAddress;
    Row row;
    Cell cell;

    public ExcelRead(AssetManager am, InputStream is){
        try {
            System.out.println(am.list("")[0]);

            //file = am.open("Mappe1.xls");
            //file2 = new POIFSFileSystem(file);
        } catch (IOException e) {
            System.out.println("EHHH");
            e.printStackTrace();

        }
        System.out.println("Passt noch");
        try {

            workbook = new XSSFWorkbook(is);
            System.out.println("IHHH");
        } catch (Exception e) {
            System.out.println("AHHH");
            e.printStackTrace();
        }

        sheet = workbook.getSheetAt(0);
        System.out.println("AHHH1");
        cellAddress = new CellAddress("A1");
        System.out.println("AHHH2");
        row = sheet.getRow(cellAddress.getRow());
        cell = row.getCell(cellAddress.getColumn());
    }

  /*  public String[] getSheet(){
        sheet = workbook.getSheetAt(0);
        for(int i = 0; i < sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);

            cell = row.getCell(cellAddress.getColumn());
        }
        return null;
    } */


    public String getData() {
        if (cell.getCellType() == CellType.FORMULA) {
            switch (cell.getCachedFormulaResultType()) {
                case NUMERIC:
                    return "" + cell.getNumericCellValue();
                case STRING:
                    return cell.getRichStringCellValue().toString();
            }

        } else{
            switch (cell.getCellType()) {
                case NUMERIC:
                    return "" + cell.getNumericCellValue();
                case STRING:
                    return cell.getRichStringCellValue().toString();
            }
        }
        return "Falsch";
    }
}