package com.example.skillhub.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration representing the progress status of a lesson or course")
public enum ProgressStatus {

    @Schema(description = "Progress has not been started")
    NOT_STARTED,

    @Schema(description = "Progress is currently incomplete")
    INCOMPLETE,

    @Schema(description = "Progress has been completed")
    COMPLETED;
}
