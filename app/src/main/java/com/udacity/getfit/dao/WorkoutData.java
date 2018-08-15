package com.udacity.getfit.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
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

    protected WorkoutData(Parcel in) {
        workoutName = in.readString();
        workoutReps = in.readString();
        time = in.readString();
        date = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  /*public static final Creator<WorkoutData> CREATOR = new Creator<WorkoutData>() {
        @Override
        public WorkoutData createFromParcel(Parcel in) {
            return new WorkoutData(in);
        }

        @Override
        public WorkoutData[] newArray(int size) {
            return new WorkoutData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(workoutName);
        parcel.writeString(workoutReps);
        parcel.writeString(time);
        parcel.writeString(date);
    }*/


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

    /*@Override
    public int describeContents() {
        return 0;
    }*/

   /* @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(workoutName);
        parcel.writeString(workoutReps);
        parcel.writeString(time);
        parcel.writeString(date );
    }

    public static final Creator<WorkoutData> CREATOR = new Creator<WorkoutData>() {
        @Override
        public WorkoutData createFromParcel(Parcel in) {
            return new WorkoutData(in);
        }

        @Override
        public WorkoutData[] newArray(int size) {
            return new WorkoutData[size];
        }
    };*/
}
