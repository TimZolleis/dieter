package dev.elektronisch.dieter.server.device;

import dev.elektronisch.dieter.common.dto.device.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/device")
public final class DeviceController {

    private final DeviceService service;
    private final ModelMapper modelMapper;

    public DeviceController(final DeviceService service, final ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register/{deviceId}")
    public ResponseEntity<DeviceRegistrationResponse> register(@PathVariable("deviceId") final UUID deviceId,
                                                               @RequestBody final DeviceRegistrationPayload payload) {
        final DeviceRegistrationResponse response = service.handleRegistration(deviceId, payload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/heartbeat/{deviceId}")
    public ResponseEntity<DeviceHeartbeatResponse> heartbeat(@PathVariable("deviceId") final UUID deviceId,
                                                             @RequestBody final DeviceHeartbeatPayload payload) {
        final DeviceHeartbeatResponse response = service.handleHeartbeat(deviceId, payload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDevice(@PathVariable("deviceId") final UUID deviceId) {
        final DeviceEntity entity = service.getDevice(deviceId);
        final Device device = convertToDTO(entity);
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @GetMapping("/all/{organisationId}")
    public ResponseEntity<Set<Device>> getDevices(@PathVariable("organisationId") final UUID organisationId) {
        final Set<Device> devices = service.getDevices(organisationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    private Device convertToDTO(final DeviceEntity entity) {
        final Device device = modelMapper.map(entity, Device.class);
        final DeviceInformation information = service.getInformation(device.getId());
        if (information != null) {
            device.setOnline(true);
            device.setLastHeartbeatAt(information.getLastHeartbeatAt());
        }
        return device;
    }
}
