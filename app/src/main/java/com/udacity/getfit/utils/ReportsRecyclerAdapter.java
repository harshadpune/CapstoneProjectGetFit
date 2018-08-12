package com.udacity.getfit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.WorkoutData;

import java.util.ArrayList;

public class ReportsRecyclerAdapter extends RecyclerView.Adapter<ReportsRecyclerAdapter.ViewHolder> {


    private final Context context;
    private final ArrayList<WorkoutData> workoutDataList;

    public ReportsRecyclerAdapter(Context context, ArrayList<WorkoutData> workoutDataList) {
        this.context = context;
        this.workoutDataList = workoutDataList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reports_list_item, null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvWorkoutName.setText(workoutDataList.get(position).getWorkoutName());
        holder.tvWorkoutTime.setText(workoutDataList.get(position).getTime());
        holder.tvWorkoutReps.setText(workoutDataList.get(position).getWorkoutReps());
        holder.tvWorkoutDate.setText(workoutDataList.get(position).getDate());
    }


    @Override
    public int getItemCount() {
        return workoutDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvWorkoutName;
        private final TextView tvWorkoutTime;
        private final TextView tvWorkoutReps;
        private final TextView tvWorkoutDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
            tvWorkoutTime = itemView.findViewById(R.id.tvWorkoutTime);
            tvWorkoutReps = itemView.findViewById(R.id.tvWorkoutReps);
            tvWorkoutDate = itemView.findViewById(R.id.tvWorkoutDate);
        }
    }
}
