package com.piranha.excelapi.cellvalue;

import com.piranha.excelapi.cellvalue.impl.BooleanReplaceCellValueApplier;
import com.piranha.excelapi.cellvalue.impl.NumericAddCellValueApplier;
import com.piranha.excelapi.cellvalue.impl.NumericReplaceCellValueApplier;
import com.piranha.excelapi.cellvalue.impl.StringReplaceCellValueApplier;
import org.apache.poi.ss.usermodel.Cell;

public class CellValueApplierFactory {
   private CellValueApplierFactory() {
      //private
   }

   public static CellValueApplier<?> getCellValueApplier(Cell cell, CellValueApplyStrategy cellValueApplyStrategy, Object value) {
      return switch (cell.getCellType()) {
         case BOOLEAN -> new BooleanReplaceCellValueApplier();
         case STRING -> new StringReplaceCellValueApplier();
         case NUMERIC -> getNumericAddCellValueApplier(cellValueApplyStrategy);
         case BLANK -> getCellValueApplierForDataType(value, cellValueApplyStrategy);
         default -> throw new IllegalStateException("Unsupported datatype '" + cell.getCellType() + "'!");
      };
   }

   private static CellValueApplier<?> getCellValueApplierForDataType(Object value, CellValueApplyStrategy cellValueApplyStrategy) {
      if (value instanceof String) {
         return new StringReplaceCellValueApplier();
      } else if (value instanceof Boolean) {
         return new BooleanReplaceCellValueApplier();
      } else if (value instanceof Number) {
         return getNumericAddCellValueApplier(cellValueApplyStrategy);
      }
      throw new IllegalStateException("Unsupported datatype '" + value + "'!");
   }

   private static CellValueApplier<Number> getNumericAddCellValueApplier(CellValueApplyStrategy cellValueApplyStrategy) {
      if (cellValueApplyStrategy == CellValueApplyStrategy.ADD) {
         return new NumericAddCellValueApplier();
      }
      return new NumericReplaceCellValueApplier();
   }
}
