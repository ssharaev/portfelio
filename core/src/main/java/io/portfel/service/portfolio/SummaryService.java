package io.portfel.service.portfolio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import io.portfel.model.Position;
import io.portfel.model.chart.ChartRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final ChartService chartService;
    private final ProfitCalculationService profitCalculationService;

    public Position getPositionResults(UUID accountId, String ticker, LocalDate from, LocalDate to) {
        ChartRecord positionChart = chartService.getPositionGraph( accountId, ticker, from, to );
        BigDecimal profit = profitCalculationService.profit( accountId, ticker, from, to );
        return new Position( ticker, BigDecimal.ZERO, BigDecimal.ZERO, profit, positionChart );
    }
}
