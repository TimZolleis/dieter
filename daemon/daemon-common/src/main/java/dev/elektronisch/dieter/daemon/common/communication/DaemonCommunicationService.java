package dev.elektronisch.dieter.daemon.common.communication;

import dev.elektronisch.dieter.client.DaemonDieterClient;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationPayload;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationResponse;
import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DaemonCommunicationService {

    private final AbstractDieterDaemonApplication application;
    private final DaemonDieterClient client;

    public DaemonCommunicationService(final AbstractDieterDaemonApplication application) {
        this.application = application;

        this.client = new DaemonDieterClient(application.getConfiguration().getDeviceKey());
        registerDevice();
    }

    private void registerDevice() {
        log.info("Registering device...");

        final DeviceRegistrationPayload payload = new DeviceRegistrationPayload();
        final DeviceRegistrationResponse response = client.register(payload);
        
    }
}
