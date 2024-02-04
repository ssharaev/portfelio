package io.portfel.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartValueData {

    private LocalDate date;
    private BigDecimal count;
    private BigDecimal currentTotal;
    private BigDecimal depositTotal;
}
