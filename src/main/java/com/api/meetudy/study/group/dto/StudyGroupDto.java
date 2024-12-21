package com.api.meetudy.study.group.dto;

import com.api.meetudy.global.util.ValidEnum;
import com.api.meetudy.study.group.enums.Location;
import com.api.meetudy.study.group.enums.StudyCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyGroupDto {

    @Schema(description = "The unique identifier of the study group.",
            example = "1")
    private Long id;

    @ValidEnum(target = StudyCategory.class)
    @Schema(description = "The category or type of the study group.",
            example = "LANGUAGE, CERTIFICATION")
    private StudyCategory category;

    @NotBlank
    @Schema(description = "The name of the study group.")
    private String name;

    @NotBlank
    @Schema(description = "Description of the study group.")
    private String description;

    @NotBlank
    @Schema(description = "The duration of the study.")
    private String duration;

    @NotNull
    @Schema(description = "Indicates whether the study is conducted online or offline.")
    private Boolean isOnline;

    @ValidEnum(target = Location.class)
    @Schema(description = "Location of the study.")
    private Location location;

    @NotNull
    @Positive
    @Schema(description = "Maximum number of participants.")
    private Integer maxParticipants;

    @NotBlank
    @Schema(description = "The method or approach used for conducting the study.")
    private String method;

}