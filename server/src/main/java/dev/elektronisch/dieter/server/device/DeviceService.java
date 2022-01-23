package dev.elektronisch.dieter.server.device;

import dev.elektronisch.dieter.common.dto.device.*;
import dev.elektronisch.dieter.server.exception.DeviceNotFoundException;
import dev.elektronisch.dieter.server.exception.DeviceNotRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

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
        final Set<DeviceInformation> devicesTimedOut = new HashSet<>();
        synchronized (informationMap) {
            informationMap.values().forEach(information -> {
                if (System.currentTimeMillis() - information.getLastHeartbeatAt() >= properties.timeoutMillis()) {
                    devicesTimedOut.add(information);
                }
            });
        }

        devicesTimedOut.forEach(information -> handleTermination(information.getEntity().getId(), TerminationReason.TIMEOUT));
    }

    public DeviceRegistrationResponse handleRegistration(final UUID deviceId, final DeviceRegistrationPayload payload) {
        final DeviceEntity entity = getDevice(deviceId);

        // Update entity
        entity.setTerminationReason(null);
        repository.save(entity);

        // Create information
        final DeviceInformation information = new DeviceInformation();
        information.setEntity(entity);
        information.setOperatingSystem(payload.getOperatingSystem());
        information.setAvailableRam(payload.getAvailableRam());
        information.setProcessorCount(payload.getProcessorCount());
        information.setRegisteredAt(System.currentTimeMillis());
        information.setLastHeartbeatAt(System.currentTimeMillis());
        informationMap.put(deviceId, information);

        log.info("Device '{} ({})' was registered", information.getEntity().getId(), information.getEntity().getName());

        // Send response
        return new DeviceRegistrationResponse(properties.heartbeatPeriodMillis());
    }

    public void handleTermination(final UUID deviceId, final TerminationReason reason) {
        final DeviceInformation information = informationMap.remove(deviceId);
        if (information == null) {
            throw new DeviceNotRegisteredException();
        }

        // Update entity
        final DeviceEntity entity = getDevice(deviceId);
        entity.setTerminationReason(reason);
        entity.setLastSeenAt(new Date());
        repository.save(entity);

        log.info("Device '{} ({})' terminated due to '{}'", information.getEntity().getId(),
                information.getEntity().getName(), reason);
    }

    public DeviceHeartbeatResponse handleHeartbeat(final UUID deviceId, final DeviceHeartbeatPayload payload) {
        final DeviceInformation information = getInformation(deviceId);
        if (information == null) {
            throw new DeviceNotRegisteredException();
        }

        // Update information
        information.setMacAddress(payload.getMacAddress());
        information.setIpAddress(payload.getIpAddress());
        information.setHostname(payload.getHostname());
        information.setUsedRam(payload.getUsedRam());
        information.setLoad(payload.getLoad());
        information.setLastHeartbeatAt(System.currentTimeMillis());
        informationMap.put(deviceId, information);

        log.info("Device '{} ({})' sent heartbeat", information.getEntity().getId(), information.getEntity().getName());

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
