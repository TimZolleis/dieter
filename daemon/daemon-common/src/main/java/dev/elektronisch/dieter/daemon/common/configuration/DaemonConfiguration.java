package dev.elektronisch.dieter.daemon.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class DaemonConfiguration {
    private String applicationVersion;
    private UUID deviceKey;
}
