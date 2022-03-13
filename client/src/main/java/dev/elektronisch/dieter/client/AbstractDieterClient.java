package dev.elektronisch.dieter.client;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import dev.elektronisch.dieter.client.api.ApiError;
import dev.elektronisch.dieter.client.api.ApiException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
@Getter
public abstract class AbstractDieterClient {

    private static final String ENDPOINT_URL = "https://api.devicedieter.de/";
    private static final ApiError EMPTY_BODY = new ApiError("EMPTY_BODY", null);
    private static final ApiError SERVICE_UNAVAILABLE = new ApiError("SERVICE_UNAVAILABLE", null);
    private static final int SERVICE_UNAVAILABLE_CODE = 503;

    private final OkHttpClient client;
    private final Gson gson;
    private final Retrofit retrofit;

    protected AbstractDieterClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final Interceptor interceptor = getInterceptor();
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        this.client = builder.build();
        this.gson = new Gson();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    protected Interceptor getInterceptor() {
        return null;
    }

    protected <T> Response<T> handleCall(final Call<T> call) throws ApiException {
        try {
            final Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                throw new ApiException(parseError(response));
            }

            return response;
        } catch (final IOException e) {
            throw new ApiException(e);
        }
    }

    protected ApiError parseError(final Response<?> response) {
        if (response.code() == SERVICE_UNAVAILABLE_CODE) {
            return SERVICE_UNAVAILABLE;
        }

        final String contentType = response.headers().get("Content-Type");
        if (contentType == null || !contentType.equals("application/json")) {
            return new ApiError("INVALID_CONTENT_TYPE", contentType + " is invalid");
        }

        if (response.errorBody() == null) {
            return EMPTY_BODY;
        }

        try {
            final String rawBody = response.errorBody().string();
            return gson.fromJson(rawBody, ApiError.class);
        } catch (final IOException | JsonParseException e) {
            return new ApiError(e);
        }
    }
}
