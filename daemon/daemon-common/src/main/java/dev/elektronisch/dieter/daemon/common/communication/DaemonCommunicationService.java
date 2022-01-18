package dev.elektronisch.dieter.daemon.common.communication;

import dev.elektronisch.dieter.client.DaemonDieterClient;
import dev.elektronisch.dieter.client.api.ApiException;
import dev.elektronisch.dieter.common.dto.device.DeviceHeartbeatPayload;
import dev.elektronisch.dieter.common.dto.device.DeviceHeartbeatResponse;
import dev.elektronisch.dieter.common.dto.device.DeviceRegistrationPayload;
import dev.elektronisch.dieter.common.dto.device.DeviceRegistrationResponse;
import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class DaemonCommunicationService {

    private final AbstractDieterDaemonApplication application;
    private final DaemonDieterClient client;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public DaemonCommunicationService(final AbstractDieterDaemonApplication application) {
        this.application = application;

        this.client = new DaemonDieterClient(application.getConfiguration().getDeviceKey());
        registerDevice();
    }

    private void registerDevice() {
        log.info("Registering device...");

        try {
            final InetAddress address = InetAddress.getLocalHost();
            final NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
            final byte[] hardwareAddress = networkInterface.getHardwareAddress();
            final String[] hexadecimal = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }

            final String macAddress = String.join("-", hexadecimal);
            final String ipAddress = address.getHostAddress();
            final String hostname = address.getHostName();
            final DeviceRegistrationPayload payload = new DeviceRegistrationPayload(macAddress, ipAddress, hostname);

            final DeviceRegistrationResponse response = client.register(payload);
            log.info("Client successfully registered! Starting heartbeat ...");

            initializeHeartbeat(response.getHeartbeatPeriodMillis());
        } catch (final ApiException | IOException e) {
            log.error("An error occurred while registering device", e);
            System.exit(1);
        }
    }

    private void initializeHeartbeat(final long periodMillis) {
        executorService.scheduleAtFixedRate(() -> {
            final DeviceHeartbeatPayload payload = new DeviceHeartbeatPayload();
            try {
                final DeviceHeartbeatResponse response = client.heartbeat(payload);
                log.info("Heartbeat performed! {}", response.getBlob());
            } catch (final ApiException e) {
                log.error("An error occurred while performing heartbeat", e);
                // TODO proper error handling and reconnection without service restart
                System.exit(1);
            }
        }, 0, periodMillis, TimeUnit.MILLISECONDS);
    }
}
