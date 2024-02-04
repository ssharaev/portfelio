package io.portfel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private UUID id;
    private UUID accountId;
    private UUID instrumentId;
    private TransactionType type;
    private TransactionStatus status;
    private String currency;
    private BigDecimal quantity;
    private BigDecimal fee;
    private BigDecimal total;
    private LocalDateTime dateTime;
}
