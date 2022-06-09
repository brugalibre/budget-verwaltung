package com.piranha.excelapi.cellvalue.impl;

import com.piranha.excelapi.cellvalue.CellValueApplier;
import com.piranha.excelapi.cellvalue.CellValueApplyStrategy;
import org.apache.poi.ss.usermodel.Cell;

public class NumericAddCellValueApplier implements CellValueApplier<Number> {
   @Override
   public void applyValue(Cell cell, Number value) {
      cell.setCellValue(cell.getNumericCellValue() + value.doubleValue());
   }

   @Override
   public CellValueApplyStrategy getCellValueApplyStrategy() {
      return CellValueApplyStrategy.ADD;
   }
}
