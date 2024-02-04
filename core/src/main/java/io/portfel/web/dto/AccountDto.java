package io.portfel.web.dto;

import io.portfel.model.AccountType;

import java.math.BigDecimal;
import java.util.List;


public record AccountDto(String id, AccountType type, BigDecimal balance, List<PositionDto> positions) {
}
