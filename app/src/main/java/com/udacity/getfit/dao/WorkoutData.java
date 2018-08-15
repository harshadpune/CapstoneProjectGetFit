package com.udacity.getfit.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "reports")
public class WorkoutData implements Serializable{

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String workoutName;
    public String workoutReps;
    public String time;
    public String date;

    public WorkoutData() {
    }

    public WorkoutData(String workoutName, String workoutReps, String time, String date) {
        this.workoutName = workoutName;
        this.workoutReps = workoutReps;
        this.time = time;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getWorkoutReps() {
        return workoutReps;
    }

    public void setWorkoutReps(String workoutReps) {
        this.workoutReps = workoutReps;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
