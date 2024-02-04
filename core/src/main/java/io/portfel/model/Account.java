package io.portfel.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private UUID id;
    private UUID userId;
    private AccountType accountType;
    private LocalDateTime createdAt;
}
