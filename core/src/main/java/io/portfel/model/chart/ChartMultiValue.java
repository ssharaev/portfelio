package io.portfel.model.chart;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ChartMultiValue(LocalDate date, List<BigDecimal> values) implements ChartValue {

}
