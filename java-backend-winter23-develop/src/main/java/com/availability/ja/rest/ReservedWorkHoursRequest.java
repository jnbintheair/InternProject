package com.availability.ja.rest;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import java.io.Serializable;
import java.sql.Time;

@NoArgsConstructor
@JsonDeserialize
@Data
@Schema
public class ReservedWorkHoursRequest implements Serializable {
    @JsonProperty("email")
    @Valid
    @Schema(description = "email", type = "string", example = "first.name@teamsparq.com")
    private String email;
    @JsonProperty("Day")
    @Valid
    @Schema(description = "Day", type = "string", example = "monday")
    private String Day;
    @JsonProperty("startTime")
    @Valid
    @Schema(description = "Start Time", type = "string", example = "09:00:00")
    private Time startTime;
    @JsonProperty("endTime")
    @Valid
    @Schema(description = "End Time", type = "string", example = "14:00:00")
    private Time endTime;

}
