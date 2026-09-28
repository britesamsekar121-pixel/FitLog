package com.java.FitLog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Weekly workout count goal must be positive")
    private Integer targetWorkouts;

    private String reason;

    private LocalDate weekStartDate = LocalDate.now();

    public Goal() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getTargetWorkouts() { return targetWorkouts; }
    public void setTargetWorkouts(Integer targetWorkouts) { this.targetWorkouts = targetWorkouts; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDate getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }
}
