package com.udacity.getfit.ui;

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
                ttobj.speak(workoutList.workoutDetails, TextToSpeech.QUEUE_FLUSH, null);
                if(lastPause == 0)
                    cmWorkoutTimer.setBase(SystemClock.elapsedRealtime());
                else
                    cmWorkoutTimer.setBase(cmWorkoutTimer.getBase() + SystemClock.elapsedRealtime()- lastPause);
                    cmWorkoutTimer.start();
                break;

            case R.id.btnStop:
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
                break;
        }
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
/*
                        for(int i=0; i< workoutDataList.size(); i++){
                            Log.d("Fragment","-----------------------workoutName "+workoutDataList.get(i).getWorkoutName());
                        }*/
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}