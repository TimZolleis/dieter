package dev.elektronisch.dieter.common.dto.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class DeviceRegistrationPayload {
    private OperatingSystem operatingSystem;
    private int availableRam;
    private int processorCount;
}
