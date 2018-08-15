package com.udacity.getfit.utils;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.udacity.getfit.R;
import com.udacity.getfit.dao.WorkoutData;
import com.udacity.getfit.database.AppDatabase;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class WorkoutReportAppWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<WorkoutData> workoutDataList) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.workout_report_app_widget);
        Intent intent = new Intent(context.getApplicationContext(), WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        Gson gson = new Gson();
        String workoutsList = gson.toJson(workoutDataList);
        intent.putExtra(AppConstants.INTENT_WORKOUT_LIST,""+workoutsList);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.lvReports, intent);
        remoteViews.setEmptyView(R.id.lvReports,R.id.empty);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        final AppDatabase mDb = AppDatabase.getInstance(context);
        final ArrayList<WorkoutData> workoutDataList = new ArrayList();
        DatabaseReference workoutReferece = FirebaseDatabase.getInstance().getReference("workouts");
        workoutReferece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equalsIgnoreCase(Utils.getCurrentUserForDB(""+ FirebaseAuth.getInstance().getCurrentUser().getEmail()))){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            WorkoutData workoutData = dataSnapshot2.getValue(WorkoutData.class);
                            workoutDataList.add(workoutData);
                        }

                        for(int i=0; i< workoutDataList.size(); i++){
                            Log.d("Fragment","------workoutName "+workoutDataList.get(i).getWorkoutName());
                        }

                        for (int appWidgetId : appWidgetIds) {
                            updateAppWidget(context, appWidgetManager, appWidgetId, workoutDataList);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

