package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDocumentDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String documentName;
    private String contentType;
}
