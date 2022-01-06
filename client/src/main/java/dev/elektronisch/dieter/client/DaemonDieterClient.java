package dev.elektronisch.dieter.client;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class DaemonDieterClient extends AbstractDieterClient {

    private final UUID deviceIdentifier;

    public DaemonDieterClient(UUID deviceIdentifier) {
        this(DEFAULT_ENDPOINT_URL, deviceIdentifier);
    }

    public DaemonDieterClient(String endpointUrl, UUID deviceIdentifier) {
        super(endpointUrl);
        this.deviceIdentifier = deviceIdentifier;
    }
}
