package com.piranha.excelapi.util;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtil {
   private static final Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);
   public static final String TMP = "tmp";

   private ExcelUtil() {
      // privé
   }

   /**
    * Evaluates all formulas for all {@link Cell}s. If there is an exception for one particular Cell,
    * this cell is ignored.
    * <p>
    * This is e.g. the case for all 'Sparkling' functions which are not implemented by the apache-poi library
    *
    * @param wb        the {@link Workbook}
    * @param evaluator the {@link FormulaEvaluator}
    */
   public static void evaluateAllFormulaCells(Workbook wb, FormulaEvaluator evaluator) {
      for (int i = 0; i < wb.getNumberOfSheets(); i++) {
         Sheet sheet = wb.getSheetAt(i);
         for (Row r : sheet) {
            for (Cell c : r) {
               if (c.getCellType() == CellType.FORMULA) {
                  try {
                     evaluator.evaluateFormulaCell(c);
                  } catch (Exception e) {
                     LOG.error("Failed formel evaluation for cell '{}'", c.getCellFormula());
                  }
               }
            }
         }
      }
   }

   /**
    * Saves the given {@link Workbook} under the given file.
    * <b>Note:</b> This method overrides the file for <code>fileName</code> if it already exists
    *
    * @param workbook the {@link Workbook} to save
    * @param fileName the name of the file, in which the workbook is stored
    * @throws IOException when there is any issue with the file or the file system
    */
   public static void save(Workbook workbook, String fileName) throws IOException {
      File destFile = new File(fileName);
      if (!destFile.exists()) {
         saveWorkbook2File(workbook, fileName);
      } else {
         String tmpFileName = fileName.replace(".", TMP + ".");
         saveWorkbook2File(workbook, tmpFileName);
         File tmpFile = new File(tmpFileName);
         FileUtils.copyFile(tmpFile, destFile);
         FileUtils.delete(tmpFile);
      }
   }

   private static void saveWorkbook2File(Workbook workbook, String fileName) throws IOException {
      try (FileOutputStream out = new FileOutputStream(fileName)) {
         workbook.write(out);
      }
   }
}
