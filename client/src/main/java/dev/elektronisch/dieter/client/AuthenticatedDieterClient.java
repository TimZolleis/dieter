package dev.elektronisch.dieter.client;

import dev.elektronisch.dieter.client.api.ApiException;
import dev.elektronisch.dieter.client.api.AuthenticationService;
import dev.elektronisch.dieter.client.api.DeviceService;
import dev.elektronisch.dieter.common.dto.authentication.LoginRequest;
import dev.elektronisch.dieter.common.dto.authentication.TokenResponse;
import dev.elektronisch.dieter.common.dto.device.Device;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Set;
import java.util.UUID;

@Slf4j
public final class AuthenticatedDieterClient extends AbstractDieterClient {

    private final String username;
    private final String password;

    private final AuthenticationService authenticationService;
    private final DeviceService deviceService;

    private String authorizationToken;
    private long tokenExpirationDate;

    public AuthenticatedDieterClient(final String username, final String password) {
        this(DEFAULT_ENDPOINT_URL, username, password);
    }

    public AuthenticatedDieterClient(final String endpointUrl, final String username, final String password) {
        super(endpointUrl);
        this.username = username;
        this.password = password;

        this.authenticationService = getRetrofit().create(AuthenticationService.class);
        this.deviceService = getRetrofit().create(DeviceService.class);
        login();
    }

    @Override
    protected Interceptor getInterceptor() {
        return chain -> {
            Request request = chain.request();
            if (authorizationToken != null) {
                request = request.newBuilder()
                        .addHeader("Authorization", "Bearer " + authorizationToken)
                        .build();
            }
            return chain.proceed(request);
        };
    }

    private void login() throws ApiException {
        final LoginRequest loginRequest = new LoginRequest(username, password);
        final Call<TokenResponse> call = authenticationService.login(loginRequest);

        final Response<TokenResponse> response = handleCall(call);
        final TokenResponse tokenResponse = response.body();
        if (tokenResponse == null) {
            return;
        }

        authorizationToken = tokenResponse.getToken();
        tokenExpirationDate = tokenResponse.getExpirationDate();
    }

    public Set<Device> getDevices(final UUID organisationId) {
        final Call<Set<Device>> call = deviceService.getDevices(organisationId);
        final Response<Set<Device>> response = handleCall(call);
        return response.body();
    }
}
