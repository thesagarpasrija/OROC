package com.word2vec.SagarProjectWord2Vec;

/*
 * ApachePOIExcelWrite class was used to create a data entry of all the replacing objects obtained for every object by varying the threshold
 * of material and shape similarity values ranging from 2 to 6.
 * The most suitable threshold values for material and shape similarity were then selected after observing the data. 
 */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ApachePOIExcelWrite {


    public static void makeExcel(ArrayList<Object[]> datatypes) {
    	
    	// path to the EXCEL file
    	final String FILE_NAME = "C:/Users/Sagar Pasrija/Desktop/ObjectsData/test.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Threshold Comparison");

        int rowNum = 0;
        System.out.println("Creating excel");

        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
                else if(field instanceof Double) {
                	cell.setCellValue((Double) field);
                }
                else if(field instanceof ArrayList<?>) {
                	cell.setCellValue(Arrays.toString(((ArrayList) field).toArray()));
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}