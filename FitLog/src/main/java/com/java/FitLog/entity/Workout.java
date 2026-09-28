package com.java.FitLog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Workout type is required")
    private String type;

    @Positive(message = "Duration must be positive")
    private Integer duration;

    @Positive(message = "Calories burnt must be positive")
    private Integer caloriesBurnt;

    private LocalDate date = LocalDate.now();

    // Default constructor
    public Workout() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Integer getCaloriesBurnt() { return caloriesBurnt; }
    public void setCaloriesBurnt(Integer caloriesBurnt) { this.caloriesBurnt = caloriesBurnt; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
