package io.portfel.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.portfel.model.TransactionStatus;
import io.portfel.model.TransactionType;
import lombok.Data;

@Data
public class CreateTransactionDto {

    private UUID instrumentId;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String currency;
    private BigDecimal quantity;
    private BigDecimal fee;
    private BigDecimal total;
    private LocalDateTime dateTime;
}
