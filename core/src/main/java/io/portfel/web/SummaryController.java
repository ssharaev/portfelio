package io.portfel.web;

import io.portfel.model.Position;
import io.portfel.service.portfolio.ProfitCalculationService;
import io.portfel.service.portfolio.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.portfel.web.dto.SummaryDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/summary")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    public SummaryDto getPortfolioSummary() {
        return new SummaryDto(
            "1",
            BigDecimal.valueOf( 25000 ),
            BigDecimal.valueOf( 2500 ),
            BigDecimal.valueOf( 10 ),
            "USD"
        );
    }

    @GetMapping(path = "/{ticker}")
    public Position getPositionSummary(@PathVariable String ticker) {
        return summaryService.getPositionResults(
            UUID.fromString( "23416940-e906-4d2d-b1e2-11c7adcd78f6" ),
            ticker,
            LocalDate.MIN,
            LocalDate.now()
        );
    }
}
