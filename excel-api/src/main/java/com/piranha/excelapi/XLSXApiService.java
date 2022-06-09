package com.piranha.excelapi;

import com.piranha.excelapi.cellvalue.CellValueApplier;
import com.piranha.excelapi.cellvalue.CellValueApplierFactory;
import com.piranha.excelapi.cellvalue.CellValueApplyStrategy;
import com.piranha.excelapi.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * The {@link XLSXApiService} creates a xls-file using a {@link HSSFWorkbook} which contains one sheet.
 *
 * @author Dominic
 */
public class XLSXApiService implements AutoCloseable {

   private static final Logger LOG = LoggerFactory.getLogger(XLSXApiService.class);
   private final FormulaEvaluator evaluator;
   private final Workbook workbook;
   private final String fileName;

   private XLSXApiService(String fileName, boolean readOnly) throws IOException {
      this.fileName = fileName;
      this.workbook = WorkbookFactory.create(new File(fileName), null, readOnly);
      this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
   }

   /**
    * Creates a new {@link XLSXApiService} for only read access to the given file
    *
    * @param fileName the xlsx-File to read from
    * @return a new {@link XLSXApiService} instance
    * @throws IOException if anything regarding the file goes wrong
    */
   public static XLSXApiService readOnly(String fileName) throws IOException {
      return new XLSXApiService(fileName, true);
   }

   /**
    * Creates a new {@link XLSXApiService} for read and write access to the given file
    *
    * @param fileName the xlsx-File to read from or write to
    * @return a new {@link XLSXApiService} instance
    * @throws IOException if anything regarding the file goes wrong
    */
   public static XLSXApiService readAndWrite(String fileName) throws IOException {
      return new XLSXApiService(fileName, false);
   }

   /**
    * Return the {@link Cell}-values for the cell, which are identified by the given colum-names and the given
    * row-name
    *
    * @param sheetName   the name of the sheet in which the cells are located
    * @param columnNames the name of the columns
    * @param rowName     the name of the row
    * @return the {@link Cell}-values
    */
   public List<com.piranha.excelapi.model.CellValue> getCellValues(String sheetName, List<String> columnNames, String rowName) {
      LOG.info("Evaluating cell for sheet name '{}', row name '{}' and column-names '{}'", sheetName, rowName, columnNames);
      List<com.piranha.excelapi.model.CellValue> cellValues = new ArrayList<>();
      for (String columnName : columnNames) {
         CellCoordinates cellCoordinates = findCell(sheetName, columnName, rowName);
         if (cellCoordinates.exists) {
            Cell cell = workbook.getSheet(sheetName).getRow(cellCoordinates.rowIndex).getCell(cellCoordinates.columnIndex);
            cellValues.add(new com.piranha.excelapi.model.CellValue(getCellValueAsString(cell), columnName));
         } else {
            LOG.error("No cell found for sheet name '{}', row name '{}' and column-names '{}'", sheetName, rowName, columnNames);
         }
      }
      LOG.info("Evaluating done! Found cell values '{}'", cellValues);
      return cellValues;
   }

   /**
    * Saves the {@link Workbook} of this {@link XLSXApiService} under the file with which this instance was created.
    * <b>Note:</b> This method overrides the file for <code>fileName</code> if it already exists
    *
    * @throws IOException when there is any issue with the file or the file system
    */
   public void save() throws IOException {
      ExcelUtil.save(workbook, fileName);
   }

   @Override
   public void close() throws Exception {
      workbook.close();
   }

   /**
    * Sets the value to the cell, which is located in the sheet with the provided name.
    * The x- and y-coordinates of this cell is defined by the column- and the row-{@link Cell}
    *
    * @param sheetName  the name of the {@link Sheet}
    * @param columnName the name of the column-{@link Cell} which defines the x-axis
    * @param rowName    the name of the row-{@link Cell} which defines the y-axis
    * @param cellValue  the value which replaces the current value
    */
   public void replaceValueForCell(String sheetName, String columnName, String rowName, Object cellValue) {
      LOG.info("Replace existing value with new {} for cell in sheet '{}', with row name '{}' and column-name '{}'", cellValue, sheetName, rowName, columnName);
      changeCellValueInternal(sheetName, columnName, rowName, cellValue, CellValueApplyStrategy.REPLACE);
   }

   /**
    * Adds the given numeric value to the cell, which is located in the sheet with the provided name.
    * The x- and y-coordinates of this cell is defined by the column- and the row-{@link Cell}
    *
    * @param sheetName  the name of the {@link Sheet}
    * @param columnName the name of the column-{@link Cell} which defines the x-axis
    * @param rowName    the name of the row-{@link Cell} which defines the y-axis
    * @param cellValue  the value which is added to the current value
    */
   public void addValueForCell(String sheetName, String columnName, String rowName, double cellValue) {
      LOG.info("Add value {} for cell in sheet '{}', with row name '{}' and column-name '{}'", cellValue, sheetName, rowName, columnName);
      changeCellValueInternal(sheetName, columnName, rowName, cellValue, CellValueApplyStrategy.ADD);
      ExcelUtil.evaluateAllFormulaCells(workbook, evaluator);
   }

   private String getCellValueAsString(Cell cell) {
      return switch (cell.getCellType()) {
         case STRING -> cell.getStringCellValue();
         case NUMERIC -> String.valueOf(cell.getNumericCellValue());
         case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
         case FORMULA -> evaluateCellValue(cell);
         case BLANK -> "";
         default -> throw new IllegalStateException("Unexpected value: " + cell.getCellType());
      };
   }

   private String evaluateCellValue(Cell cell) {
      try {
         return evaluator.evaluate(cell).formatAsString();
      } catch (Exception e) {
         String errorMsg = "Unable to evaluate formel for cell %s".formatted(cell);
         LOG.error(errorMsg, e);
      }
      return "-";
   }

   private void changeCellValueInternal(String sheetName, String columnName, String rowName, Object cellValue, CellValueApplyStrategy cellValueApplyStrategy) {
      CellCoordinates cellCoordinates = findCell(sheetName, columnName, rowName);
      if (cellCoordinates.exists) {
         Cell cell = workbook.getSheet(sheetName).getRow(cellCoordinates.rowIndex).getCell(cellCoordinates.columnIndex);
         LOG.info("Cell '{}' found, apply value {} with applier {}", cell, cellValue, cellValueApplyStrategy);
         setCellValue(cell, cellValue, cellValueApplyStrategy);
      } else {
         LOG.error("Can't apply new value, no cell found for sheet name '{}', row name '{}' and column-name '{}'!", sheetName, rowName, columnName);
      }
   }

   private void setCellValue(Cell cell, Object cellValue, CellValueApplyStrategy cellValueApplyStrategy) {
      CellValueApplier cellValueApplier = CellValueApplierFactory.getCellValueApplier(cell, cellValueApplyStrategy, cellValue);
      cellValueApplier.applyValue(cell, cellValue);
      evaluator.notifyUpdateCell(cell);
   }

   private CellCoordinates findCell(String sheetName, String columnName, String rowName) {
      Sheet sheet = workbook.getSheet(sheetName);
      Cell rowCell = findCellByName(rowName, sheet);
      Cell columnCell = findCellByName(columnName, sheet);
      if (nonNull(rowCell) && nonNull(columnCell)) {
         return CellCoordinates.found(rowCell.getRowIndex(), columnCell.getColumnIndex());
      }
      return CellCoordinates.notFound();
   }

   private static Cell findCellByName(String rowName, Sheet sheet) {
      Iterator<Row> rowIterator = sheet.rowIterator();
      while (rowIterator.hasNext()) {
         for (Cell cell : rowIterator.next()) {
            if (hasCellStringValue(cell, rowName)) {
               return cell;
            }
         }
      }
      return null;
   }

   private static boolean hasCellDataType(Cell firstRowCell, CellType cellType) {
      return nonNull(firstRowCell) && firstRowCell.getCellType() == cellType;
   }

   private static boolean hasCellStringValue(Cell cell, String value) {
      return hasCellDataType(cell, CellType.STRING) && cell.getStringCellValue().equals(value);
   }

   private record CellCoordinates(int rowIndex, int columnIndex, boolean exists) {
      private static CellCoordinates notFound() {
         return new CellCoordinates(-1, -1, false);
      }

      private static CellCoordinates found(int rowIndex, int columnIndex) {
         return new CellCoordinates(rowIndex, columnIndex, true);
      }
   }
}