package com.udacity.getfit.dao;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FitnessData {

    @SerializedName("dailyVideo")
    public String dailyVideo;
    @SerializedName("workout")
    public List<WorkoutInformation> workoutInformation = new ArrayList();

    public class WorkoutInformation{
        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public String type;
        @SerializedName("imageUrl")
        public String imageUrl;
        @SerializedName("workoutList")
        public List<WorkoutList> workoutLists = new ArrayList();

    }

    public class WorkoutList {
        @SerializedName("workoutName")
        public String workoutName;
        @SerializedName("workoutReps")
        public String workoutReps;
    }
}
