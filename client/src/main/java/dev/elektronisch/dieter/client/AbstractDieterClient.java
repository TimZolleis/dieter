package dev.elektronisch.dieter.client;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import dev.elektronisch.dieter.client.api.ApiError;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
@Getter
public abstract class AbstractDieterClient {

    protected static final String DEFAULT_ENDPOINT_URL = "http://localhost:8080/";
    private static final ApiError UNKNOWN_ERROR = new ApiError("UNKNOWN_ERROR", null);

    private final String endpointUrl;
    private final OkHttpClient client;
    private final Gson gson;
    private final Retrofit retrofit;

    protected AbstractDieterClient(final String endpointUrl) {
        this.endpointUrl = endpointUrl;

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final Interceptor interceptor = getInterceptor();
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        this.client = builder.build();
        this.gson = new Gson();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(endpointUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    protected Interceptor getInterceptor() {
        return null;
    }

    protected ApiError parseError(final Response<?> response) {
        final String contentType = response.headers().get("Content-Type");
        if (contentType == null || !contentType.equals("application/json")) {
            return UNKNOWN_ERROR;
        }

        if (response.errorBody() == null) {
            return UNKNOWN_ERROR;
        }

        try {
            final String rawBody = response.errorBody().string();
            return gson.fromJson(rawBody, ApiError.class);
        } catch (IOException | JsonParseException e) {
            log.error("An exception occurred while parsing error", e);
            return UNKNOWN_ERROR;
        }
    }
}
