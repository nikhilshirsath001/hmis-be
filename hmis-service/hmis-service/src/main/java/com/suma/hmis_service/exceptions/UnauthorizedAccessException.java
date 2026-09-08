package com.suma.hmis_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(final String message) {
        super(message);
    }
}
