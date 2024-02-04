package io.portfel.web.dto;

import java.math.BigDecimal;

public record InstrumentDto(String id, String name, String symbol, BigDecimal price, String currency) {
}
