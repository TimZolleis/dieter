package dev.elektronisch.dieter.client;

import dev.elektronisch.dieter.client.api.ApiException;
import dev.elektronisch.dieter.client.api.DeviceService;
import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatPayload;
import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatResponse;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationPayload;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.util.UUID;

@Getter
@Slf4j
public final class DaemonDieterClient extends AbstractDieterClient {

    private final UUID deviceId;
    private final DeviceService deviceService;

    public DaemonDieterClient(final UUID deviceIdentifier) {
        this(DEFAULT_ENDPOINT_URL, deviceIdentifier);
    }

    public DaemonDieterClient(final String endpointUrl, final UUID deviceIdentifier) {
        super(endpointUrl);
        this.deviceId = deviceIdentifier;
        this.deviceService = getRetrofit().create(DeviceService.class);
    }

    public DeviceRegistrationResponse register(final DeviceRegistrationPayload payload) throws ApiException {
        final Call<DeviceRegistrationResponse> call = deviceService.register(deviceId, payload);
        final Response<DeviceRegistrationResponse> response = handleCall(call);
        return response.body();
    }

    public DeviceHeartbeatResponse heartbeat(final DeviceHeartbeatPayload payload) throws ApiException {
        final Call<DeviceHeartbeatResponse> call = deviceService.heartbeat(deviceId, payload);
        final Response<DeviceHeartbeatResponse> response = handleCall(call);
        return response.body();
    }
}
