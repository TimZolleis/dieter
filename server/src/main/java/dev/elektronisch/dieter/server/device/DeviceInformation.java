package dev.elektronisch.dieter.server.device;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public final class DeviceInformation {
    private DeviceEntity entity;
    private long lastHeartbeatAt;
    private long registeredAt;
}
