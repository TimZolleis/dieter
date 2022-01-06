package dev.elektronisch.dieter.common.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class VerificationRequest {
    private UUID uuid;
}
