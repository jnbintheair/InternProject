package com.availability.ja.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Entity
@Table(name = "ReservedWorkHours")
@NoArgsConstructor
@Data
public class ReservedWorkHours implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="ReservedWorkHoursID")
  private Long ReservedWorkHoursID;
  @Column(name="userID")
  private Long userID;
  @Column(name="startTime")
  private Time startTime;

  @Column(name="endTime")
  private Time endTime;
  @Column(name="DayOfWeekID")
  private Long DayOfWeekID;

}
