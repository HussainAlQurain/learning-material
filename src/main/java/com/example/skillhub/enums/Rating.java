package com.example.skillhub.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration representing the rating scale for courses, lessons, or authors")
public enum Rating {

    @Schema(description = "Rating of 1 out of 5")
    ONE,

    @Schema(description = "Rating of 2 out of 5")
    TWO,

    @Schema(description = "Rating of 3 out of 5")
    THREE,

    @Schema(description = "Rating of 4 out of 5")
    FOUR,

    @Schema(description = "Rating of 5 out of 5")
    FIVE;
}
