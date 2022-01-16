package dev.elektronisch.dieter.common.model.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class DeviceRegistrationPayload {
    private String macAddress;
    private String ipAddress;
    private String hostname;
}
