package com.piranha.excelapi.cellvalue.impl;

import com.piranha.excelapi.cellvalue.CellValueApplier;
import org.apache.poi.ss.usermodel.Cell;

public class BooleanReplaceCellValueApplier implements CellValueApplier<String> {
   @Override
   public void applyValue(Cell cell, String value) {
      cell.setCellValue(value);
   }
}
