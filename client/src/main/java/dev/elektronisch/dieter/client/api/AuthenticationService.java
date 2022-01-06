package dev.elektronisch.dieter.client.api;

import dev.elektronisch.dieter.common.model.authentication.LoginRequest;
import dev.elektronisch.dieter.common.model.authentication.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {
    @POST("login")
    Call<TokenResponse> login(@Body LoginRequest request);
}
