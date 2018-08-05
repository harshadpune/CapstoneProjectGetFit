package com.udacity.getfit.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.getfit.R;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.report);
    }
}
