package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("DEVICE_NOT_FOUND")
public final class DeviceNotFoundException extends RuntimeException {
}
