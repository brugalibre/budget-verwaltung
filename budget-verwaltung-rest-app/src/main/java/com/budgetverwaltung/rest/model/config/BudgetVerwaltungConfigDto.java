package com.budgetverwaltung.rest.model.config;

import com.budgetverwaltung.util.YamlUtil;

import java.util.List;

import static com.budgetverwaltung.rest.app.config.BudgetVerwaltungRestAppConfig.BUDGET_VERWALTUNG_CONF_FILE;


public class BudgetVerwaltungConfigDto {
   private List<ChartConfigDto> charts;
   private String xlsxFilePath;
   private String sheetName;

   public static BudgetVerwaltungConfigDto get() {
      return YamlUtil.readYaml(BUDGET_VERWALTUNG_CONF_FILE, BudgetVerwaltungConfigDto.class);
   }

   public String getXlsxFilePath() {
      return xlsxFilePath;
   }

   public String getSheetName() {
      return sheetName;
   }

   public void setSheetName(String sheetName) {
      this.sheetName = sheetName;
   }

   public void setXlsxFilePath(String xlsxFilePath) {
      this.xlsxFilePath = xlsxFilePath;
   }

   public List<ChartConfigDto> getCharts() {
      return charts;
   }

   public void setCharts(List<ChartConfigDto> charts) {
      this.charts = charts;
   }

   public List<String> getChartNames() {
      return charts.stream()
              .map(ChartConfigDto::getChartName)
              .toList();
   }

   public ChartConfigDto getChartConfigDto4ChartName(String chartName) {
      return charts.stream()
              .filter(chartConfigDto -> chartConfigDto.getChartName().equals(chartName))
              .findFirst()
              .orElseThrow(() -> new IllegalStateException("No ChartConfig for name " + chartName));
   }

   public List<String> getColumns4ChartName(String rowName) {
      return getChartConfigDto4ChartName(rowName)
              .getColumnNames();
   }
}
