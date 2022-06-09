package com.budgetverwaltung.rest.model.chart;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record ChartDto(String chartName, List<ColumnDto> columnDtos, String xAxisLabel, String yAxisLabel) {
}
