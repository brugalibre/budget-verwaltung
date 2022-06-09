package com.piranha.excelapi;

import com.piranha.excelapi.model.CellValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class XLSXApiServiceTest {

   private static final String COLUMN_2 = "Column2";
   private static final String TABELLENBLATT_1 = "Tabellenblatt1";
   private static final String SHEET_NAME = TABELLENBLATT_1;
   private static final String ROW_3 = "Row3";
   private static final String SUM_ROW_NAME = "Summe";
   private static final int DEFAULT_VALUE = 11;
   public static final String TEST_TABLE_XLSX = "TestTable.xlsx";
   public static final String COLUMN_3 = "Column3";

   @AfterEach
   public void reset() throws IOException {
      XLSXApiService xlsxApiService = XLSXApiService.readAndWrite(TEST_TABLE_XLSX);
      xlsxApiService.replaceValueForCell(SHEET_NAME, COLUMN_2, ROW_3, DEFAULT_VALUE);
      xlsxApiService.replaceValueForCell(SHEET_NAME, COLUMN_3, ROW_3, "");
      xlsxApiService.save();
   }

   @Test
   void readAndReplaceExistingValue() throws IOException {
      // Given
      List<String> columNames = List.of("Column0", "Column1", COLUMN_2, COLUMN_3);
      int increment = 50;
      String expectedValue = "50.0";
      String expectedSumValue = "72008.0";

      // When
      XLSXApiService xlsxApiService = XLSXApiService.readAndWrite(TEST_TABLE_XLSX);
      xlsxApiService.replaceValueForCell(SHEET_NAME, COLUMN_2, ROW_3, increment);

      CellValue newCellValue = getCellValue4Index(xlsxApiService, SHEET_NAME, columNames, ROW_3, 2);
      CellValue sumCell = getCellValue4Index(xlsxApiService, SHEET_NAME, columNames, SUM_ROW_NAME, 2);
      xlsxApiService.save();

      // Then
      assertThat(newCellValue.cellValue(), is(expectedValue));
      assertThat(sumCell.cellValue(), is(expectedSumValue));
   }

   @Test
   void readAndReplaceEmptyValue() throws IOException {
      // Given
      List<String> columNames = List.of("Column0", "Column1", COLUMN_2, COLUMN_3);
      int increment = 50;
      String expectedValue = "50.0";
      String expectedSumValue = "44859.0";

      // When
      XLSXApiService xlsxApiService = XLSXApiService.readAndWrite(TEST_TABLE_XLSX);
      xlsxApiService.replaceValueForCell(SHEET_NAME, COLUMN_3, ROW_3, increment);

      CellValue newCellValue = getCellValue4Index(xlsxApiService, SHEET_NAME, columNames, ROW_3, 3);
      CellValue sumCell = getCellValue4Index(xlsxApiService, SHEET_NAME, columNames, SUM_ROW_NAME, 3);

      // Then
      assertThat(newCellValue.cellValue(), is(expectedValue));
      assertThat(sumCell.cellValue(), is(expectedSumValue));
   }

   @Test
   void readAndAddNewToExistingValue() throws IOException {
      // Given
      String sheetName = "Tabellenblatt1";
      List<String> columNames = List.of("Column0", "Column1", COLUMN_2, COLUMN_3);
      String rowName = "Row3";
      int increment = 50;
      String expectedValue = "61.0";
      String expectedSumValue = "72019.0";

      // When
      XLSXApiService xlsxApiService = XLSXApiService.readAndWrite(TEST_TABLE_XLSX);
      xlsxApiService.addValueForCell(sheetName, COLUMN_2, rowName, increment);
      CellValue sumCell = getCellValue4Index(xlsxApiService, SHEET_NAME, columNames, SUM_ROW_NAME, 2);
      xlsxApiService.save();

      CellValue newCellValue = getCellValue4Index(xlsxApiService, sheetName, columNames, rowName, 2);

      // Then
      assertThat(newCellValue.cellValue(), is(expectedValue));
      assertThat(sumCell.cellValue(), is(expectedSumValue));
   }

   private CellValue getCellValue4Index(XLSXApiService xlsxApiService, String sheetName, List<String> columNames, String rowName, int index) {
      List<CellValue> cellValues = xlsxApiService.getCellValues(sheetName, columNames, rowName);
      return cellValues.get(index);
   }
}