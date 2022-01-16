package dev.elektronisch.dieter.server.device;

import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatPayload;
import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatResponse;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationPayload;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class DeviceService {

    private final DeviceRepository repository;

    public DeviceService(final DeviceRepository repository) {
        this.repository = repository;
    }

    public DeviceRegistrationResponse handleRegistration(final UUID deviceId, final DeviceRegistrationPayload payload) {
        return null;
    }

    public DeviceHeartbeatResponse handleHeartbeat(final UUID deviceId, final DeviceHeartbeatPayload payload) {
        return null;
    }
}
