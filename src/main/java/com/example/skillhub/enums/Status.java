package com.example.skillhub.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration representing the status of various entities like courses, authors, or sections")
public enum Status {

    @Schema(description = "Entity is active and operational")
    ACTIVE,

    @Schema(description = "Entity is inactive and not operational")
    INACTIVE,

    @Schema(description = "Entity is pending approval or activation")
    PENDING,

    @Schema(description = "Entity has been approved")
    APPROVED,

    @Schema(description = "Entity has been rejected")
    REJECTED;
}
