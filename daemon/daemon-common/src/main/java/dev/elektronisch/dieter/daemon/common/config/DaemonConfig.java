package dev.elektronisch.dieter.daemon.common.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public final class DaemonConfig {
    private String applicationVersion;
    private UUID deviceKey;
}
