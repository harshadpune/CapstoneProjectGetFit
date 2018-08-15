package com.udacity.getfit.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.udacity.getfit.dao.WorkoutData;

import java.util.List;

/**
 * Created by HARSHAD on 15/08/2018.
 */

@Dao
public interface ReportsDao {

    @Query("SELECT * FROM reports")
    LiveData<List<WorkoutData>> loadAllReports();

    /*@Delete
    void deleteMovie(MoviesData moviesData);*/

    @Insert
    void insertWorkoutData(WorkoutData workoutData);

    @Insert
    void insertWorkoutDataList(List<WorkoutData> workoutDataList);

    @Query("DELETE FROM reports")
    void deleteAllReports();

   /* @Query("SELECT * FROM movies WHERE id=:id")
    LiveData<MoviesData> getMovieById(String id);*/
}
