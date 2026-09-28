package com.java.FitLog.controller;

import com.java.FitLog.entity.Goal;
import com.java.FitLog.entity.Meal;
import com.java.FitLog.entity.Workout;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// =================================================================
// 1. REPOSITORIES (Database access)
// =================================================================
interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByDate(LocalDate date);
}

interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByDate(LocalDate date);
}

interface GoalRepository extends JpaRepository<Goal, Long> {
}

// =================================================================
// 2. REST CONTROLLER (Endpoints)
// =================================================================
@RestController
@RequestMapping("/api")
public class FitLogController {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private GoalRepository goalRepository;

    @PostMapping("/workouts")
    public ResponseEntity<?> logWorkout(@Valid @RequestBody Workout workout) {
        if (workout.getCaloriesBurnt() < 0) return ResponseEntity.badRequest().body("Calories burnt must be non-negative.");
        return ResponseEntity.ok(workoutRepository.save(workout));
    }

    @PostMapping("/meals")
    public ResponseEntity<?> logMeal(@Valid @RequestBody Meal meal) {
        if (meal.getCalories() < 0) return ResponseEntity.badRequest().body("Calories consumed must be non-negative.");
        if (meal.getQuantity() != null && meal.getQuantity() > 0) {
            meal.setCalories(meal.getCalories() * meal.getQuantity());
        }
        return ResponseEntity.ok(mealRepository.save(meal));
    }

    @GetMapping("/summary/daily")
    public ResponseEntity<?> getDailySummary() {
        LocalDate today = LocalDate.now();
        List<Workout> todayWorkouts = workoutRepository.findByDate(today);
        List<Meal> todayMeals = mealRepository.findByDate(today);

        int totalBurnt = todayWorkouts.stream().mapToInt(Workout::getCaloriesBurnt).sum();
        int totalConsumed = todayMeals.stream().mapToInt(Meal::getCalories).sum();
        Goal latestGoal = goalRepository.findAll().stream().reduce((first, second) -> second).orElse(null);

        Map<String, Object> summary = new HashMap<>();
        summary.put("date", today);
        summary.put("caloriesConsumed", totalConsumed);
        summary.put("caloriesBurnt", totalBurnt);
        summary.put("netCalories", totalConsumed - totalBurnt);
        summary.put("todayWorkouts", todayWorkouts);
        summary.put("todayMeals", todayMeals);
        summary.put("latestGoal", latestGoal);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/weekly")
    public ResponseEntity<?> getWeeklySummary() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        List<Workout> wList = workoutRepository.findAll().stream().filter(w -> !w.getDate().isBefore(oneWeekAgo)).toList();
        List<Meal> mList = mealRepository.findAll().stream().filter(m -> !m.getDate().isBefore(oneWeekAgo)).toList();
        
        int totalBurnt = wList.stream().mapToInt(Workout::getCaloriesBurnt).sum();
        int totalConsumed = mList.stream().mapToInt(Meal::getCalories).sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("workoutsLast7Days", wList.size());
        summary.put("weeklyCaloriesConsumed", totalConsumed);
        summary.put("weeklyCaloriesBurnt", totalBurnt);
        summary.put("weeklyNetCalories", totalConsumed - totalBurnt);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/goals")
    public ResponseEntity<?> setGoal(@Valid @RequestBody Goal goal) {
        if (goal.getTargetWorkouts() < 0) return ResponseEntity.badRequest().body("Goal must be non-negative.");
        return ResponseEntity.ok(goalRepository.save(goal));
    }
}

// =================================================================
// 3. GLOBAL EXCEPTION HANDLER
// =================================================================
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body("Validation Error: " + errorMessage);
    }
}
