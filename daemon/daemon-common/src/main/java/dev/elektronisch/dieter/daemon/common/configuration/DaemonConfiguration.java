package dev.elektronisch.dieter.daemon.common.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public final class DaemonConfiguration {
    private String applicationVersion;
    private UUID deviceKey;
}
