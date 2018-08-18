package com.udacity.getfit.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface WorkoutsDao {

    @Query("SELECT * FROM workout")
    LiveData<WorkoutMaster> loadAllData();

    @Query("DELETE FROM workout")
    void deleteAllData();

    @Insert
    void insertWorkoutMaster(WorkoutMaster workoutMaster);

}
