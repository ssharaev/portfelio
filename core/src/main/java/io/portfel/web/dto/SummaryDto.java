package io.portfel.web.dto;

import java.math.BigDecimal;

public record SummaryDto(
        String userId,
        BigDecimal balance,
        BigDecimal totalInvestment,
        BigDecimal profitPercentage,
        String currency) {
}
