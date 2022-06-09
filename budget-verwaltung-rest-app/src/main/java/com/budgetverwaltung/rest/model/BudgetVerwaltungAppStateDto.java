package com.budgetverwaltung.rest.model;


import com.budgetverwaltung.rest.model.config.BudgetVerwaltungConfigDto;

import java.util.List;

public class BudgetVerwaltungAppStateDto {
   private final String stagingMsg;
   private final List<String> chartNames;

   public BudgetVerwaltungAppStateDto() {
      BudgetVerwaltungConfigDto budgetVerwaltungConfigDto = BudgetVerwaltungConfigDto.get();
      this.chartNames = budgetVerwaltungConfigDto.getChartNames();
      this.stagingMsg = "Budget-Verwaltung 2.0 Webapplikation";
   }

   public String getStagingMsg() {
      return stagingMsg;
   }

   public List<String> getChartNames() {
      return chartNames;
   }
}
