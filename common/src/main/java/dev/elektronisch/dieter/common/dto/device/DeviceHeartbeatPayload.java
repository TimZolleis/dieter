package dev.elektronisch.dieter.common.dto.device;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public final class DeviceHeartbeatPayload {
    private String macAddress;
    private String ipAddress;
    private String hostname;

    private int usedRam;
    private float load;
}
