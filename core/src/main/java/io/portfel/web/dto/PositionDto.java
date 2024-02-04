package io.portfel.web.dto;

import java.math.BigDecimal;

public record PositionDto(String id,
                          InstrumentDto instrument,
                          BigDecimal quantity,
                          BigDecimal amount) {
}
