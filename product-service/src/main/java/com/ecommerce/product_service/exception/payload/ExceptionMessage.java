package com.ecommerce.product_service.exception.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.ecommerce.product_service.constant.AppConstant;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = AppConstant.ZONED_DATE_TIME_FORMAT
    )
    private final ZonedDateTime timestamp;

    private final int status;

    private final String error;

    private final String errorCode;

    private final String message;

    private final String path;

    /**
     * Useful for tracing logs in production
     */
    private final String traceId;

    /**
     * Validation errors
     */
    private final Map<String, String> errors;
}