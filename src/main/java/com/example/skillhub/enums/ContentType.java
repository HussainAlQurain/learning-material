package com.example.skillhub.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration representing the type of content in a section")
public enum ContentType {

    @Schema(description = "Textual content")
    TEXT,

    @Schema(description = "Image content")
    IMAGE,

    @Schema(description = "Video content")
    VIDEO,

    @Schema(description = "Audio content")
    AUDIO,

    @Schema(description = "PDF document content")
    PDF,

    @Schema(description = "Quiz content")
    QUIZ,

    @Schema(description = "Other types of content")
    OTHER;
}
