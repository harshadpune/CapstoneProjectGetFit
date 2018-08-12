package com.udacity.getfit.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.WorkoutData;
import com.udacity.getfit.utils.ReportsRecyclerAdapter;
import com.udacity.getfit.utils.Utils;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView rvReport;
    private ArrayList<WorkoutData> workoutDataList;
    private DatabaseReference workoutReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_report);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.report);
        initComponents();
        checkIfWorkoutIsPresent();
    }

    private void initComponents() {
        workoutReference = FirebaseDatabase.getInstance().getReference("workouts");
        rvReport = findViewById(R.id.rvReport);
        workoutDataList = new ArrayList();
    }

    private void checkIfWorkoutIsPresent() {
        workoutReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+ FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            WorkoutData workoutData = dataSnapshot2.getValue(WorkoutData.class);
                            workoutDataList.add(workoutData);
                        }

                        for(int i=0; i< workoutDataList.size(); i++){
                            Log.d("Fragment","-----------------------workoutName "+workoutDataList.get(i).getWorkoutName());
                        }
                    }
                }

                setReportsData(workoutDataList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setReportsData(ArrayList<WorkoutData> workoutDataList) {
        if(workoutDataList.size() != 0)
            rvReport.setAdapter(new ReportsRecyclerAdapter(ReportActivity.this, workoutDataList));
        else
            Toast.makeText(this, "No data available!!", Toast.LENGTH_SHORT).show();
    }
}
