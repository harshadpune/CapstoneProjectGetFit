package com.udacity.getfit.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;
import com.udacity.getfit.dao.WorkoutData;
import com.udacity.getfit.utils.AppConstants;
import com.udacity.getfit.utils.Utils;
import com.udacity.getfit.utils.WorkoutReportAppWidget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FitnessDetailsFragment extends Fragment implements View.OnClickListener {

    private FitnessData.WorkoutList workoutList;
    private TextToSpeech ttobj;
    private TextView tvWorkoutName;
    private TextView tvWorkoutDetails;
    private Button btnStart;
    private Button btnStop;
    private Chronometer cmWorkoutTimer;
    private long lastPause = 0;
    private WorkoutDetailsActivity mParentActivity;
    private ArrayList<WorkoutData> workoutDataList;
    private LinearLayout llCompleteContainer;
    private TextView tvReps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_workout_details, container, false);
        Bundle bundle = getArguments();
        workoutList = (FitnessData.WorkoutList) bundle.getSerializable(AppConstants.SELECTED_WORKOUT_DETAILS);
        tvWorkoutName = rootView.findViewById(R.id.tvWorkoutName);
        tvWorkoutDetails = rootView.findViewById(R.id.tvWorkoutDetails);
        cmWorkoutTimer = rootView.findViewById(R.id.cmWorkoutTimer);
        btnStart = rootView.findViewById(R.id.btnStart);
        btnStop = rootView.findViewById(R.id.btnStop);
        tvReps = rootView.findViewById(R.id.tvReps);
        llCompleteContainer = rootView.findViewById(R.id.llCompleteContainer);
        mParentActivity = (WorkoutDetailsActivity) getActivity();
        workoutDataList = new ArrayList();
        setData();
        setListeners();
        checkIfWorkoutIsPresent();
        return rootView;
    }

    private void setListeners() {
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    private void setData() {
        tvWorkoutName.setText(workoutList.workoutName);
        tvWorkoutDetails.setText(workoutList.workoutDetails);
        tvReps.setText(getString(R.string.reps)+""+workoutList.workoutReps);
        tvReps.setContentDescription(getString(R.string.cd_reps)+" "+workoutList.workoutReps);
        ttobj=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttobj.setLanguage(Locale.US);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnStart:
                btnStop.setEnabled(true);
                btnStart.setEnabled(false);
                ttobj.speak(workoutList.workoutDetails, TextToSpeech.QUEUE_FLUSH, null);
                if(lastPause == 0)
                    cmWorkoutTimer.setBase(SystemClock.elapsedRealtime());
                else
                    cmWorkoutTimer.setBase(cmWorkoutTimer.getBase() + SystemClock.elapsedRealtime()- lastPause);
                    cmWorkoutTimer.start();
                break;

            case R.id.btnStop:
                btnStop.setEnabled(false);
                btnStart.setVisibility(View.INVISIBLE);
                btnStop.setVisibility(View.INVISIBLE);
                llCompleteContainer.setVisibility(View.VISIBLE);
                lastPause = SystemClock.elapsedRealtime();
                cmWorkoutTimer.stop();
                Date todaysDate = Calendar.getInstance().getTime();
                SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy");
               /* if(workoutDataList.size() ==0) {
                    ArrayList<WorkoutData> workoutDataList = new ArrayList();
                }*/
                WorkoutData workoutData = new WorkoutData(workoutList.workoutName, workoutList.workoutReps, cmWorkoutTimer.getText().toString(), sf.format(todaysDate) );
                workoutDataList.add(workoutData);
                mParentActivity.workoutReference.child(Utils.getCurrentUserForDB(""+ FirebaseAuth.getInstance().getCurrentUser().getEmail())).setValue(workoutDataList);
                updateAppWidgets();
                break;
        }
    }

    private void updateAppWidgets() {

        Intent intent = new Intent(mParentActivity, WorkoutReportAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(mParentActivity).getAppWidgetIds(new ComponentName(mParentActivity, WorkoutReportAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);
    }

    public void onPause(){
        if(ttobj !=null){
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onPause();
    }

    private void checkIfWorkoutIsPresent() {

        mParentActivity.workoutReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            WorkoutData workoutData = dataSnapshot2.getValue(WorkoutData.class);
                            workoutDataList.add(workoutData);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}