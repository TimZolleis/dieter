package dev.elektronisch.dieter.common.dto.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class DeviceHeartbeatResponse {
    private String blob;
}
