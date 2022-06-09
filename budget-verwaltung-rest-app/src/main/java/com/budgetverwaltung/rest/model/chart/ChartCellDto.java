package com.budgetverwaltung.rest.model.chart;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record ChartCellDto(String value, String name) {
}
