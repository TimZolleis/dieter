package dev.elektronisch.dieter.client.api;

import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatPayload;
import dev.elektronisch.dieter.common.model.device.DeviceHeartbeatResponse;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationPayload;
import dev.elektronisch.dieter.common.model.device.DeviceRegistrationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.UUID;

public interface DeviceService {
    @POST("device/register/{deviceId}")
    Call<DeviceRegistrationResponse> register(@Path("deviceId") UUID deviceId,
                                              @Body DeviceRegistrationPayload payload);

    @POST("device/heartbeat/{deviceId}")
    Call<DeviceHeartbeatResponse> heartbeat(@Path("deviceId") UUID deviceId,
                                            @Body DeviceHeartbeatPayload payload);
}
