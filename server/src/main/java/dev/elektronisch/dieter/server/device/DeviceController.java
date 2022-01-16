package dev.elektronisch.dieter.server.device;

import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatPayload;
import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatResponse;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationPayload;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/device")
public final class DeviceController {

    private final DeviceService service;

    public DeviceController(final DeviceService service) {
        this.service = service;
    }

    @PostMapping("/register/{deviceId}")
    public ResponseEntity<DeviceRegistrationResponse> register(@PathVariable final UUID deviceId,
                                                               @RequestBody final DeviceRegistrationPayload payload) {
        final DeviceRegistrationResponse response = service.handleRegistration(deviceId, payload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/heartbeat/{deviceId}")
    public ResponseEntity<DeviceHeartbeatResponse> heartbeat(@PathVariable final UUID deviceId,
                                                             @RequestBody final DeviceHeartbeatPayload payload) {
        final DeviceHeartbeatResponse response = service.handleHeartbeat(deviceId, payload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/all/{organisationId}")
    public ResponseEntity<Void> getDevices(@PathVariable final UUID organisationId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
