package com.ecommerce.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductMetaDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String metaKey;
    private String metaValue;

    // ─── Audit ───────────────────────────────────────────────────────────────
    private Instant createdAt;
    private Instant updatedAt;
}
