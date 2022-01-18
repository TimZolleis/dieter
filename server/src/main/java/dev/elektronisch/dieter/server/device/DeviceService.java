package dev.elektronisch.dieter.server.device;

import dev.elektronisch.dieter.common.dto.device.DeviceHeartbeatPayload;
import dev.elektronisch.dieter.common.dto.device.DeviceHeartbeatResponse;
import dev.elektronisch.dieter.common.dto.device.DeviceRegistrationPayload;
import dev.elektronisch.dieter.common.dto.device.DeviceRegistrationResponse;
import dev.elektronisch.dieter.server.exception.DeviceNotFoundException;
import dev.elektronisch.dieter.server.exception.DeviceNotRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public final class DeviceService {

    private final DeviceRepository repository;
    private final DeviceConfigurationProperties properties;

    private final Map<UUID, DeviceInformation> informationMap = new HashMap<>();

    public DeviceService(final DeviceRepository repository,
                         final DeviceConfigurationProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }

    @Scheduled(fixedRate = 1000)
    private void checkDeviceTimeouts() {
        synchronized (informationMap) {
            informationMap.values().removeIf(information -> {
                if (System.currentTimeMillis() - information.getLastHeartbeatAt() >= properties.timeoutMillis()) {
                    log.info("Device '{} ({})' timed out!", information.getEntity().getId(), information.getEntity().getHostname());
                    return true;
                }
                return false;
            });
        }
    }

    public DeviceRegistrationResponse handleRegistration(final UUID deviceId, final DeviceRegistrationPayload payload) {
        final DeviceEntity entity = getDevice(deviceId);

        // Update entity
        entity.setMacAddress(payload.getMacAddress());
        entity.setIpAddress(payload.getIpAddress());
        entity.setHostname(payload.getHostname());
        repository.save(entity);

        // Create information
        final DeviceInformation information = new DeviceInformation();
        information.setEntity(entity);
        information.setRegisteredAt(System.currentTimeMillis());
        information.setLastHeartbeatAt(System.currentTimeMillis());
        informationMap.put(deviceId, information);

        log.info("Device '{} ({})' registered!", information.getEntity().getId(), information.getEntity().getHostname());

        // Send response
        return new DeviceRegistrationResponse(properties.heartbeatPeriodMillis());
    }

    public void handleShutdown(final UUID deviceId) {
        final DeviceInformation information = informationMap.remove(deviceId);
        if (information == null) {
            throw new DeviceNotRegisteredException();
        }

        log.info("Device '{} ({})' reported shutdown!", information.getEntity().getId(), information.getEntity().getHostname());
    }

    public DeviceHeartbeatResponse handleHeartbeat(final UUID deviceId, final DeviceHeartbeatPayload payload) {
        final DeviceInformation information = getInformation(deviceId);
        if (information == null) {
            throw new DeviceNotRegisteredException();
        }

        information.setLastHeartbeatAt(System.currentTimeMillis());
        informationMap.put(deviceId, information);

        log.info("Device '{} ({})' reported heartbeat!", information.getEntity().getId(), information.getEntity().getHostname());

        // TODO build response
        return new DeviceHeartbeatResponse("test");
    }

    public DeviceEntity getDevice(final UUID deviceId) {
        return repository.findById(deviceId).orElseThrow(DeviceNotFoundException::new);
    }

    public Set<DeviceEntity> getDevices(final UUID organisationId) {
        return repository.findAllByOrganisationId(organisationId);
    }

    public DeviceInformation getInformation(final UUID deviceId) {
        return informationMap.get(deviceId);
    }
}
