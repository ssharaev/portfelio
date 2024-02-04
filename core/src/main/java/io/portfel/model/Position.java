package io.portfel.model;

import java.math.BigDecimal;

import io.portfel.model.chart.ChartRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {

    private String ticker;
    private BigDecimal quantity;
    private BigDecimal total;
    private BigDecimal profit;
    private ChartRecord chart;

}
