package com.udacity.getfit.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;
import com.udacity.getfit.utils.AppConstants;
import com.udacity.getfit.utils.WorkoutListRecyclerAdapter;

public class GetFitWorkoutListActivity extends AppCompatActivity {

    private RecyclerView rvWorkoutList;
    private ImageView ivWorkout;
    private TextView tvWorkoutType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_get_fit_workout_list);
        initComponents();
        setData();
    }


    private void initComponents() {
        rvWorkoutList = findViewById(R.id.rvWorkoutList);
        ivWorkout = findViewById(R.id.ivWorkout);
        tvWorkoutType = findViewById(R.id.tvWorkoutType);
    }

    private void setData() {
        FitnessData.WorkoutInformation workoutInformation = (FitnessData.WorkoutInformation) getIntent().getSerializableExtra(AppConstants.INTENT_WORKOUT_INFO);
        tvWorkoutType.setText(workoutInformation.type);
        Picasso.with(this).load(workoutInformation.imageUrl).into(ivWorkout);
        WorkoutListRecyclerAdapter workoutListRecyclerAdapter = new WorkoutListRecyclerAdapter(this, workoutInformation);
        rvWorkoutList.setAdapter(workoutListRecyclerAdapter);
    }
}
