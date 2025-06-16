package com.fitness.booking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Checkin {
    private Integer checkinId;
    private Integer userId;
    private LocalDate checkinDate;
    private Double weight;
    private Double sleepHours;
    private Integer waterCups;
    private Integer steps;
    private String mood;
    private Boolean completedPlanToday;
    private String generalNotes;
    private LocalDateTime createdAt;

    // 构造函数
    public Checkin() {}

    public Checkin(Integer userId, LocalDate checkinDate) {
        this.userId = userId;
        this.checkinDate = checkinDate;
    }

    // Getter和Setter方法
    public Integer getCheckinId() {
        return checkinId;
    }

    public void setCheckinId(Integer checkinId) {
        this.checkinId = checkinId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(Double sleepHours) {
        this.sleepHours = sleepHours;
    }

    public Integer getWaterCups() {
        return waterCups;
    }

    public void setWaterCups(Integer waterCups) {
        this.waterCups = waterCups;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Boolean getCompletedPlanToday() {
        return completedPlanToday;
    }

    public void setCompletedPlanToday(Boolean completedPlanToday) {
        this.completedPlanToday = completedPlanToday;
    }

    public String getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(String generalNotes) {
        this.generalNotes = generalNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 