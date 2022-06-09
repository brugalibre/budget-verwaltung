package com.budgetverwaltung.rest.service;

import com.budgetverwaltung.rest.api.XLSXException;
import com.budgetverwaltung.rest.model.chart.ChartCellDto;
import com.budgetverwaltung.rest.model.chart.ChartDto;
import com.budgetverwaltung.rest.model.chart.ColumnDto;
import com.budgetverwaltung.rest.model.chart.ColumnType;
import com.budgetverwaltung.rest.model.config.BudgetVerwaltungConfigDto;
import com.budgetverwaltung.rest.model.config.ChartConfigDto;
import com.piranha.excelapi.XLSXApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetVerwaltungService {

   private static final Logger LOG = LoggerFactory.getLogger(BudgetVerwaltungService.class);

   /**
    * Adds the given numeric value to the cell, defined by the name of the column (x-axs value) and the name of
    * the row (y-axis value)
    *
    * @param columnName the name of the column
    * @param rowName    the name of the row
    * @param value      the value to add
    */
   public void addNumericCellValue(String columnName, String rowName, double value) {
      BudgetVerwaltungConfigDto budgetVerwaltungConfigDto = BudgetVerwaltungConfigDto.get();
      try (XLSXApiService xlsxApiService = XLSXApiService.readAndWrite(budgetVerwaltungConfigDto.getXlsxFilePath())) {
         xlsxApiService.addValueForCell(budgetVerwaltungConfigDto.getSheetName(), columnName, rowName, value);
         xlsxApiService.save();
      } catch (Exception e) {
         LOG.error("Exception when adding number!", e);
         throw new XLSXException(e);
      }
   }

   /**
    * Returns the chart-data, as a List of String-Arrays, which contains the cell-values for a monthly based value. Meaning,
    * we assume that there are column for each month of the year
    * and the specified columns, for which the cell-value is requested
    *
    * @param rowName the name of the row whose data is requested
    * @return a List of 2-dimensional String-Arrays
    */
   public List<String[]> getChartData(String rowName) {
      BudgetVerwaltungConfigDto budgetVerwaltungConfigDto = BudgetVerwaltungConfigDto.get();
      List<ChartCellDto> chartCellDtos;
      try (XLSXApiService xlsxApiService = XLSXApiService.readOnly(budgetVerwaltungConfigDto.getXlsxFilePath())) {
         chartCellDtos = xlsxApiService.getCellValues(budgetVerwaltungConfigDto.getSheetName(), budgetVerwaltungConfigDto.getColumns4ChartName(rowName), rowName)
                 .stream().map(cellValue -> new ChartCellDto(cellValue.cellValue(), cellValue.cellName()))
                 .toList();
      } catch (Exception e) {
         LOG.error("Exception when adding number!", e);
         throw new XLSXException(e);
      }
      return map2ListOfArray(chartCellDtos);
   }

   /*
    * Vue-js expects an array of values. One array for each entry
    */
   private static List<String[]> map2ListOfArray(List<ChartCellDto> chartCellDtos) {
      return chartCellDtos.stream()
              .map(chartCellDto -> new String[]{chartCellDto.name(), chartCellDto.value()})
              .toList();
   }

   public ChartDto getChartDto(String chartName) {
      ChartConfigDto chartConfigDto = BudgetVerwaltungConfigDto.get()
              .getChartConfigDto4ChartName(chartName);
      return getChartDtoFromChartConfig(chartConfigDto);
   }

   private static ChartDto getChartDtoFromChartConfig(ChartConfigDto chartConfigDto) {
      List<ColumnDto> columnDtos = new ArrayList<>();
      LocalDate now = LocalDate.now();
      int i = 1;
      for (String columnName : chartConfigDto.getColumnNames()) {
         boolean isSelected = chartConfigDto.getColumnType() == ColumnType.MONTH && now.getMonthValue() == i;
         columnDtos.add(new ColumnDto(columnName, columnName, isSelected));
         i++;
      }
      return new ChartDto(chartConfigDto.getChartName(), columnDtos, chartConfigDto.getxAxisLabel(), chartConfigDto.getyAxisLabel());
   }
}
