package com.piranha.excelapi.cellvalue;

import org.apache.poi.ss.usermodel.Cell;

public interface CellValueApplier<T> {
   /**
    * Applies the given Value to the given cell
    *
    * @param cell  the {@link Cell} whose value is changed
    * @param value the new value
    */
   void applyValue(Cell cell, T value);

   /**
    * @return the {@link CellValueApplyStrategy} of this {@link CellValueApplier}
    */
   default CellValueApplyStrategy getCellValueApplyStrategy() {
      return CellValueApplyStrategy.REPLACE;
   }
}
