package com.budgetverwaltung.rest.model.config;

import com.budgetverwaltung.rest.model.chart.ColumnType;

import java.util.List;

public class ChartConfigDto {
   private String chartName;
   private String xAxisLabel;
   private String yAxisLabel;
   private ColumnType columnType;
   private List<String> columnNames;

   public String getChartName() {
      return chartName;
   }

   public void setChartName(String chartName) {
      this.chartName = chartName;
   }

   public List<String> getColumnNames() {
      return columnNames;
   }

   public void setColumnNames(List<String> columnNames) {
      this.columnNames = columnNames;
   }

   public ColumnType getColumnType() {
      return columnType;
   }

   public void setColumnType(ColumnType columnType) {
      this.columnType = columnType;
   }

   public String getxAxisLabel() {
      return xAxisLabel;
   }

   public void setxAxisLabel(String xAxisLabel) {
      this.xAxisLabel = xAxisLabel;
   }

   public String getyAxisLabel() {
      return yAxisLabel;
   }

   public void setyAxisLabel(String yAxisLabel) {
      this.yAxisLabel = yAxisLabel;
   }
}
