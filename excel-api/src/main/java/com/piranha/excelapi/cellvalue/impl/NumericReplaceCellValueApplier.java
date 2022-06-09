package com.piranha.excelapi.cellvalue.impl;

import com.piranha.excelapi.cellvalue.CellValueApplier;
import org.apache.poi.ss.usermodel.Cell;

public class NumericReplaceCellValueApplier implements CellValueApplier<Number> {
   @Override
   public void applyValue(Cell cell, Number value) {
      cell.setCellValue(value.doubleValue());
   }
}
