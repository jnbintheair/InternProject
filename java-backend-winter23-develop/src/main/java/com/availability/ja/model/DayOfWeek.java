package com.availability.ja.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Entity
@Table(name = "DayOfWeek")
@NoArgsConstructor
@Data
public class DayOfWeek implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="DayOfWeekID")
  private Long DayOfWeekID;
  @Column(name="Day")
  private String Day;

}
