package com.udacity.getfit.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.io.Serializable;

@Entity(tableName = "workout")
public class WorkoutMaster implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int masterid;
    public String workoutMasterData;

    @NonNull
    public int getMasterid() {
        return masterid;
    }

    public void setMasterid(@NonNull int masterid) {
        this.masterid = masterid;
    }

    public String getWorkoutMasterData() {
        return workoutMasterData;
    }

    public void setWorkoutMasterData(String workoutMasterData) {
        this.workoutMasterData = workoutMasterData;
    }
}
