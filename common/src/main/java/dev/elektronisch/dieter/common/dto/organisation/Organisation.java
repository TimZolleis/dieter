package dev.elektronisch.dieter.common.dto.organisation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class Organisation {
    private UUID id;
    private String name;
    private long createdAt;
}
