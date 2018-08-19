package com.udacity.getfit.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;
import com.udacity.getfit.ui.WorkoutDetailsActivity;

public class WorkoutListRecyclerAdapter extends RecyclerView.Adapter<WorkoutListRecyclerAdapter.ViewHolder> {
    private final Context context;
    private final FitnessData.WorkoutInformation workoutInformation;

    public WorkoutListRecyclerAdapter(Context context, FitnessData.WorkoutInformation workoutInformation){
        this.context = context;
        this.workoutInformation = workoutInformation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workout_list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvWorkoutName.setText(""+workoutInformation.workoutLists.get(position).workoutName);
            holder.tvWorkoutReps.setText(context.getString(R.string.reps) +" "+workoutInformation.workoutLists.get(position).workoutReps);
            holder.tvWorkoutReps.setContentDescription(context.getString(R.string.cd_reps)+" "+workoutInformation.workoutLists.get(position).workoutReps);
    }

    @Override
    public int getItemCount() {
        return workoutInformation.workoutLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvWorkoutName;
        private final TextView tvWorkoutReps;
        private final LinearLayout llWorkoutInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            llWorkoutInfo = itemView.findViewById(R.id.llWorkoutInfo);
            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
            tvWorkoutReps = itemView.findViewById(R.id.tvWorkoutReps);

            llWorkoutInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.llWorkoutInfo:
                    Intent workoutDetailsActivity  = new Intent(context, WorkoutDetailsActivity.class);
                    workoutDetailsActivity.putExtra(AppConstants.SELECTED_WORKOUT, getAdapterPosition());
                    workoutDetailsActivity.putExtra(AppConstants.INTENT_WORKOUT_INFO, workoutInformation);
                    context.startActivity(workoutDetailsActivity);
                    break;
            }
        }
    }
}
