package dev.elektronisch.dieter.server.device;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("device")
public final record DeviceConfigurationProperties(long heartbeatPeriodMillis, long timeoutMillis) {
}
