package dev.elektronisch.dieter.common.dto.device;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public final class Device {
    private UUID id;
    private UUID organisationId;
    private String macAddress;
    private String ipAddress;
    private String hostname;
    private long createdAt;
    private long modifiedAt;
    private TerminationReason terminationReason;

    private boolean online;
    private long lastHeartbeatAt;
}
