package com.mariuszilinskas.eshopapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mariuszilinskas.eshopapi.util.AppUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppUtils.TIMESTAMP_FORMAT)
    private final ZonedDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;

    public ErrorResponse(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
        timestamp = ZonedDateTime.now();
    }

}
