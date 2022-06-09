package com.budgetverwaltung.rest.app.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.budgetverwaltung"})
public class BudgetVerwaltungRestAppConfig {

   public static final String BUDGET_VERWALTUNG_CONF_FILE = "config/budget-verwaltung-config.yml";
}

