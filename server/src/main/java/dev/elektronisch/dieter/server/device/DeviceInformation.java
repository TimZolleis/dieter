package dev.elektronisch.dieter.server.device;

import dev.elektronisch.dieter.common.dto.device.OperatingSystem;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public final class DeviceInformation {
    private DeviceEntity entity;

    private String macAddress;
    private String ipAddress;
    private String hostname;
    private long lastHeartbeatAt;
    private long registeredAt;

    private OperatingSystem operatingSystem;
    private int availableRam;
    private int usedRam;
    private int processorCount;
    private float load;
}
