package com.availability.ja.rest;

import com.availability.ja.model.ReservedWorkHours;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@JsonDeserialize
@Data
public class ReservedWorkHoursListRequest<T> {

    @JsonProperty("data")
    private JsonNode data;
    @JsonProperty("email")
    private String email;

    public ReservedWorkHoursListRequest() {
    }

    public ReservedWorkHoursListRequest(JsonNode data) {
        this.data = data;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;


    }
}
