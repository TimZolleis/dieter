package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("DEVICE_ALREADY_REGISTERED")
public final class DeviceAlreadyRegisteredException extends RuntimeException {
}
