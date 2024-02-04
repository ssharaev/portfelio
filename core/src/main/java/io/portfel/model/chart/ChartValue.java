package io.portfel.model.chart;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ChartValue {

    LocalDate date();

    List<BigDecimal> values();
}
