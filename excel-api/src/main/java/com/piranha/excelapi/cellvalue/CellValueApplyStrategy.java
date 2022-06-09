package com.piranha.excelapi.cellvalue;

public enum CellValueApplyStrategy {
   /**
    * Replaces an existing value of a cell
    */
   REPLACE,
   /**
    * Adds a value to an existing cell value. <b>Note:</b> This works only with numeric values
    */
   ADD
}
