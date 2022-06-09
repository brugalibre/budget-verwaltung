package com.budgetverwaltung.rest.api;

import com.budgetverwaltung.rest.model.ApplicationTitleDto;
import com.budgetverwaltung.rest.model.BudgetVerwaltungAppStateDto;
import com.budgetverwaltung.rest.model.chart.ChartDto;
import com.budgetverwaltung.rest.service.BudgetVerwaltungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/budget-verwaltung")
@RestController
public class BudgetVerwaltungController {

   private final BudgetVerwaltungService budgetVerwaltungService;

   @Autowired
   public BudgetVerwaltungController(BudgetVerwaltungService budgetVerwaltungService) {
      this.budgetVerwaltungService = budgetVerwaltungService;
   }

   @GetMapping(path = "/getBudgetVerwaltungAppStateDto")
   public BudgetVerwaltungAppStateDto getBudgetVerwaltungAppStateDto() {
      return new BudgetVerwaltungAppStateDto();
   }

   @GetMapping(path = "/getApplicationTitle")
   public ApplicationTitleDto getApplicationTitle() {
      return new ApplicationTitleDto("Budget-Verwaltung 2.0");
   }

   @GetMapping(path = "/getChartDto/{chartName}")
   public ChartDto getChartDto(@PathVariable final String chartName) {
      return budgetVerwaltungService.getChartDto(chartName);
   }

   @GetMapping(path = "/getChartData/{chartName}")
   public List<String[]> getChartData(@PathVariable final String chartName) {
      return budgetVerwaltungService.getChartData(chartName);
   }

   @PostMapping(path = "/addCellValue/{chartName}")
   public int addCellValue(@PathVariable final String chartName, @RequestParam(name = "columnName") final String columnName,
                           @RequestParam(name = "value") final double value) {
      budgetVerwaltungService.addNumericCellValue(columnName, chartName, value);
      return HttpStatus.OK.value();
   }
}
