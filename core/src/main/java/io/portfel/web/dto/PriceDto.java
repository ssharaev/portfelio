package io.portfel.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceDto (String symbol,
     BigDecimal price,
     LocalDateTime dateTime) {}

