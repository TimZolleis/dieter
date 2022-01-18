package dev.elektronisch.dieter.client.api;

import dev.elektronisch.dieter.common.dto.device.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Set;
import java.util.UUID;

public interface DeviceService {
    @POST("device/register/{deviceId}")
    Call<DeviceRegistrationResponse> register(@Path("deviceId") UUID deviceId,
                                              @Body DeviceRegistrationPayload payload);

    @POST("device/heartbeat/{deviceId}")
    Call<DeviceHeartbeatResponse> heartbeat(@Path("deviceId") UUID deviceId,
                                            @Body DeviceHeartbeatPayload payload);

    @GET("device/all/{organisationId}")
    Call<Set<Device>> getDevices(@Path("organisationId") UUID organisationId);
}
