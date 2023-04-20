package com.availability.ja.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.sql.Time;

@NoArgsConstructor
@JsonDeserialize
@Data
public class UpdateReservedWorkHoursRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("email")
    @Valid
    private String email;
    @JsonProperty("Day")
    @Valid
    private String Day;
    @JsonProperty("startTime")
    @Valid
    @Schema(description = "Start Time", type = "string", example = "09:00:00")
    private Time startTime;
    @JsonProperty("endTime")
    @Valid
    @Schema(description = "End Time", type = "string", example = "09:00:00")
    private Time endTime;
    @JsonProperty("newstartTime")
    @Valid
    @Schema(description = "New Start Time", type = "string", example = "09:00:00")
    private Time newstartTime;
    @JsonProperty("newendTime")
    @Valid
    @Schema(description = "New End Time", type = "string", example = "09:00:00")
    private Time newendTime;
}
