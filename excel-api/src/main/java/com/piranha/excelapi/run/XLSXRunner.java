package com.piranha.excelapi.run;

import com.piranha.excelapi.XLSXApiService;
import com.piranha.excelapi.model.CellValue;

import java.util.Arrays;
import java.util.List;

public class XLSXRunner {

   public static void main(String[] args) {
      try (XLSXApiService xlsxApiService = XLSXApiService.readAndWrite(args[0])) {
         xlsxApiService.addValueForCell(args[1], args[2], args[3], 50);
         List<CellValue> cellValues = xlsxApiService.getCellValues(args[1], Arrays.asList(args[4].split(",")), args[3]);
         printCellValues(cellValues);
         xlsxApiService.save();
      } catch (Exception e) {
         e.printStackTrace();
         throw new IllegalStateException(e);
      }
   }

   private static void printCellValues(List<CellValue> cellValues) {
      for (CellValue cellValue : cellValues) {
         System.out.print(cellValue.cellName() + "\t\t");
      }
      System.out.println();
      for (CellValue cellValue : cellValues) {
         System.out.print(cellValue.cellValue() + "\t");
      }
      System.out.println();
   }
}
