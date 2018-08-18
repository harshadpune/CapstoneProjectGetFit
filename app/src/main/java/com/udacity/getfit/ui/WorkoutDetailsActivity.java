package com.udacity.getfit.ui;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;
import com.udacity.getfit.databinding.ActivityWorkoutDetailsBinding;
import com.udacity.getfit.utils.AppConstants;

public class WorkoutDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ActivityWorkoutDetailsBinding activityWorkoutDetailsBinding;
    private FitnessData.WorkoutInformation workoutInformation;
    private int selectedWorkout;
    public DatabaseReference workoutReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_workout_details);
        getSupportActionBar().setTitle(R.string.workout_details);
        activityWorkoutDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_workout_details);
        workoutInformation = (FitnessData.WorkoutInformation) getIntent().getSerializableExtra(AppConstants.INTENT_WORKOUT_INFO);
        selectedWorkout =  getIntent().getIntExtra(AppConstants.SELECTED_WORKOUT,0);
        workoutReference = FirebaseDatabase.getInstance().getReference("workouts");
        setData();
    }


    private void setData() {
        activityWorkoutDetailsBinding.pager.setAdapter(new FitnessDetailsPagerAdapter(getSupportFragmentManager()));
        activityWorkoutDetailsBinding.pager.setCurrentItem(selectedWorkout);
        activityWorkoutDetailsBinding.pager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class FitnessDetailsPagerAdapter extends FragmentStatePagerAdapter {
        public FitnessDetailsPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            FitnessDetailsFragment fitnessDetailsFragment = new FitnessDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstants.SELECTED_WORKOUT_DETAILS, workoutInformation.workoutLists.get(position));
            fitnessDetailsFragment.setArguments(bundle);
            return fitnessDetailsFragment;
        }

        @Override
        public int getCount() {
            return workoutInformation.workoutLists.size();
        }
    }
}
