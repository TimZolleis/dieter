package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("ORGANISATION_NOT_FOUND")
public final class OrganisationNotFoundException extends RuntimeException {
}
