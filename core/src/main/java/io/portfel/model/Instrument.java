package io.portfel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {

    // TODO add all data like region, field, etc.
    private UUID id;
    private String ticker;
    private String name;
    private UUID exchangeId;
    private LocalDateTime createdAt;
}
