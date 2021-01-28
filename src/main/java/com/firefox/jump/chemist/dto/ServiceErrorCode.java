package com.firefox.jump.chemist.dto;

public enum ServiceErrorCode {

    UNEXPECTED_ERROR(1, "Unexpected error."),
    // Http 4xx - 5xx
    UNAUTHORIZED(401, "401 Unauthorized"),
    NOT_FOUND(404, "404 Not found"),
    INTERNAL_ERROR(500, "Internal Error"),
    BAD_REQUEST(400, "Bad request"),

    // Customized 1001 - 1999
    INVALID_TOKEN(10001, "Invalid token"),
    INVALID_CREDENTIAL(2002, "Invalid username or password."),

    // Common 10001 - 19999
    REQUEST_TIME_OUT(10002, "The request timed out."),
    RESOURCE_NOT_FOUND(10003, "Resource not found."),
    DUPLICATED_VALUE(10004, "Duplicate valueList already exist."),
    DATA_INTEGRITY_VIOLATION(10005, "Data integrity violation exception."),

    INVALID_PARAMETER(10006, "Invalid parameter."),
    OBJECT_NOT_FOUND(10007, "Object Not Found"),

    IO_EXCEPTION(10008, "IOException"),
    ILLEGAL_STATE(10009, "Illegal State"),

;





    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private final int code;
    private final String message;

    ServiceErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
