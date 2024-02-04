package io.portfel.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsByDay implements Comparable<TransactionsByDay> {

    private LocalDate date;
    private BigDecimal total;
    private BigDecimal count;

    @Override
    public int compareTo(TransactionsByDay o) {
        return date.compareTo( o.date );
    }
}
