package io.portfel.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Price {

    private String ticker;
    private BigDecimal price;
    private LocalDateTime dateTime;

}
