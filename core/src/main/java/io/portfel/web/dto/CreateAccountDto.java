package io.portfel.web.dto;

import java.util.UUID;

import io.portfel.model.AccountType;
import lombok.Data;

@Data
public class CreateAccountDto {

    private UUID userId;
    private AccountType type;
}
