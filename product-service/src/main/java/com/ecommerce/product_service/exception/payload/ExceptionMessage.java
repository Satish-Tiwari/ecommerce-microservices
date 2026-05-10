package com.ecommerce.product_service.exception.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.ecommerce.product_service.constant.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ExceptionMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.ZONED_DATE_TIME_FORMAT)
    private final ZonedDateTime timestamp;

    private final int status;
    private final String error;
    private final String errorCode;
    private final String message;
    private final String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<String, String> errors;

}
