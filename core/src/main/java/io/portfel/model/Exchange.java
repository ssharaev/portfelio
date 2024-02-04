package io.portfel.model;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

    private UUID id;
    private String shortName;
    private String fullName;
}
