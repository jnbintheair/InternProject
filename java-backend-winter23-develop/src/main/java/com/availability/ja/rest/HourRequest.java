package com.availability.ja.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.sql.Time;

@NoArgsConstructor
@JsonDeserialize
@Data
@Schema
public class HourRequest implements Serializable {
    @JsonProperty("hour")
    @Valid
    @Schema(description = "Time", type = "string", example = "09:00:00")
    private Time hour;
    @JsonProperty("day")
    @Valid
    private String day;
}
