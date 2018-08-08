package com.udacity.getfit.ui;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.FitnessData;
import com.udacity.getfit.utils.AppConstants;
import java.util.Locale;

public class FitnessDetailsFragment extends Fragment implements View.OnClickListener {

    private FitnessData.WorkoutList workoutList;
    private TextToSpeech ttobj;
    private TextView tvWorkoutName;
    private TextView tvWorkoutDetails;
    private Button btnStart;
    private Button btnStop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_workout_details, container, false);
        Bundle bundle = getArguments();
        workoutList = (FitnessData.WorkoutList) bundle.getSerializable(AppConstants.SELECTED_WORKOUT_DETAILS);
        tvWorkoutName = rootView.findViewById(R.id.tvWorkoutName);
        tvWorkoutDetails = rootView.findViewById(R.id.tvWorkoutDetails);
        btnStart = rootView.findViewById(R.id.btnStart);
        btnStop = rootView.findViewById(R.id.btnStop);

        setData();
        setListeners();
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
                break;

            case R.id.btnStop:

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
}