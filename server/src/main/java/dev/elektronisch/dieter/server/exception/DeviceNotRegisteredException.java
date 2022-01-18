package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("DEVICE_NOT_REGISTERED")
public final class DeviceNotRegisteredException extends RuntimeException {
}
