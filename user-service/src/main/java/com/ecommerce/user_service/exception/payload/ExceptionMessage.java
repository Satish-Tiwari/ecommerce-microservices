package com.ecommerce.user_service.exception.payload;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import com.ecommerce.user_service.constant.AppConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExceptionMessage implements Serializable {
    @Serial
    private static final Long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.ZONED_DATE_TIME_FORMAT)
    private final ZonedDateTime timestamp;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Throwable throwable;

    private final HttpStatus httpStatus;

    private final String msg;
}