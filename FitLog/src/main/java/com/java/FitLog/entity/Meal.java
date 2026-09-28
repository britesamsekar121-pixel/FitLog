package com.java.FitLog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Food item is required")
    private String foodItem;

    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @Positive(message = "Calories consumed must be positive")
    private Integer calories;

    private LocalDate date = LocalDate.now();

    // Default constructor
    public Meal() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFoodItem() { return foodItem; }
    public void setFoodItem(String foodItem) { this.foodItem = foodItem; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
